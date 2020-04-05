/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 *
 *
 */
package net.sourceforge.plantuml.classdiagram.command;

import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.IRegex;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Ident;
import net.sourceforge.plantuml.skin.VisibilityModifier;

public class CommandAddMethod extends SingleLineCommand2<ClassDiagram> {

	public CommandAddMethod() {
		super(getRegexConcat());
	}

	static IRegex getRegexConcat() {
		return RegexConcat.build(CommandAddMethod.class.getName(), RegexLeaf.start(), //
				new RegexLeaf("NAME", "([\\p{L}0-9_.]+|[%g][^%g]+[%g])"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf(":"), //
				RegexLeaf.spaceOneOrMore(), //
				new RegexLeaf("DATA", "(.*)"), //
				RegexLeaf.end()); //
	}

	@Override
	protected CommandExecutionResult executeArg(ClassDiagram diagram, LineLocation location, RegexResult arg) {
		final String idShort = arg.get("NAME", 0);
		final Ident ident = diagram.buildLeafIdent(idShort);
		final Code code = diagram.V1972() ? ident : diagram.buildCode(idShort);
		/* final */IEntity entity;
		if (diagram.V1972()) {
			entity = diagram.getLeafVerySmart(diagram.cleanIdent(ident));
			if (entity == null) {
				entity = diagram.getOrCreateLeaf(ident, code, null, null);
			}
		} else {
			entity = diagram.getOrCreateLeaf(ident, code, null, null);
		}

		final String field = arg.get("DATA", 0);
		if (field.length() > 0 && VisibilityModifier.isVisibilityCharacter(field)) {
			diagram.setVisibilityModifierPresent(true);
		}
		entity.getBodier().addFieldOrMethod(field);
		return CommandExecutionResult.ok();
	}
}
