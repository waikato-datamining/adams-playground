/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * Commit.java
 * Copyright (C) 2024 University of Waikato, Hamilton, New Zealand
 */

package adams.gui.flow.menu.git;

import adams.core.io.FileUtils;
import adams.gui.action.AbstractBaseAction;
import adams.gui.core.GUIHelper;
import org.eclipse.jgit.api.Status;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.logging.Level;

/**
 * Performs a "git commit".
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class Commit
  extends AbstractFlowEditorGitMenuItem {

  private static final long serialVersionUID = -2869403192412073396L;

  /**
   * Creates the action to use.
   *
   * @return		the action
   */
  @Override
  protected AbstractBaseAction newAction() {
    return new AbstractBaseAction("Commit") {
      @Override
      protected void doActionPerformed(ActionEvent e) {
	String file = m_Owner.getCurrentFile().getAbsolutePath();
	String msg = GUIHelper.showInputDialog(m_Owner, "Commit message for " + m_Owner.getCurrentFile().getName() + ":");
	if (msg == null)
	  return;
	try {
	  m_Git.commit()
	    .setOnly(file)
	    //.setCommitter("name", "email")  // TODO read from .gitconfig?
	    .setMessage(msg)
	    .call();
	}
	catch (Exception ex) {
	  getLogger().log(Level.SEVERE, "Failed to commit: " + file, e);
	  GUIHelper.showErrorMessage(m_Owner, "Failed to commit:\n" + file, ex);
	}
      }
    };
  }

  /**
   * Updating the action/menuitem/submenu, based on the current status of the owner.
   */
  @Override
  public void update() {
    Status 	status;
    File 	curFile;
    String	curRelPath;

    curFile    = m_Owner.getCurrentFile();
    curRelPath = FileUtils.relativePath(m_Git.getRepository().getWorkTree(), curFile);
    try {
      status = m_Git.status()
		 .addPath(curRelPath)
		 .call();
      m_Action.setEnabled(
	status.getModified().contains(curRelPath)
	  || status.getAdded().contains(curRelPath));
    }
    catch (Exception e) {
      m_Action.setEnabled(false);
      getLogger().log(Level.SEVERE, "Failed to query status of repo!", e);
    }
  }
}
