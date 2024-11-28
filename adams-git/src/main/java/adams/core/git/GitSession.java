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
 * GitSession.java
 * Copyright (C) 2024 University of Waikato, Hamilton, New Zealand
 */

package adams.core.git;

import adams.core.io.FileUtils;
import adams.core.logging.CustomLoggingLevelObject;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.RepositoryBuilder;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages the repositories during a session.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class GitSession
  extends CustomLoggingLevelObject {

  private static final long serialVersionUID = -1973439331943188604L;

  /** the git repos. */
  protected Map<File, Git> m_Repos;

  /** the file repo/false relation. */
  protected Map<File, Object> m_Controlled;

  /** the singleton. */
  protected static GitSession m_Singleton;

  /**
   * Initializes the session object.
   */
  protected GitSession() {
    m_Repos      = new HashMap<>();
    m_Controlled = new HashMap<>();
  }

  /**
   * Adds the git repo.
   *
   * @param git		the repo to add
   */
  public void addRepo(Git git) {
    m_Repos.put(git.getRepository().getWorkTree(), git);
  }

  /**
   * Returns the currently managed repos.
   *
   * @return		the repos
   */
  public Collection<Git> repos() {
    return m_Repos.values();
  }

  /**
   * Returns the repo for the specified dir/file.
   *
   * @param path	the dir/file to get the repo for
   * @return		the repo, null if no repo (yet) available
   */
  public Git repoFor(File path) {
    Git		result;
    String 	pathStr;
    String	parentStr;

    result  = null;
    pathStr = FileUtils.useForwardSlashes(path.getAbsolutePath());
    for (File parent: m_Repos.keySet()) {
      parentStr = FileUtils.useForwardSlashes(parent.getAbsolutePath()) + "/";
      if (pathStr.startsWith(parentStr)) {
	result = m_Repos.get(parent);
	break;
      }
    }

    return result;
  }

  /**
   * Checks whether the dir/file is inside a git-managed directory sub-tree.
   * Automatically calls {@link #addRepo(Git)} when it is within a git-controlled sub-tree.
   *
   * @param path	the dir/file to check
   * @return		true if under control
   * @see		#addRepo(Git) 
   */
  public boolean isWithinRepo(File path) {
    RepositoryBuilder 	builder;
    Git			git;

    if (!m_Controlled.containsKey(path)) {
      // already a repo present?
      git = repoFor(path);

      // new repo?
      if (git == null) {
	try {
	  if (path.isDirectory())
	    builder = new RepositoryBuilder().findGitDir(path);
	  else
	    builder = new RepositoryBuilder().findGitDir(path.getParentFile());
	  git = new Git(builder.build());
	  getLogger().info("path: " + path);
	  getLogger().info("-> git repo dir: " + git.getRepository().getWorkTree());
	  addRepo(git);
	}
	catch (Exception e) {
	  // expecting RepositoryNotFoundException if not a repo
	  m_Controlled.put(path, false);
	}
      }

      // update cache
      if (git != null)
	m_Controlled.put(path, git);
      else
	m_Controlled.put(path, false);
    }

    return (m_Controlled.get(path) instanceof Git);
  }

  /**
   * Returns the session singleton.
   *
   * @return		the singleton
   */
  public static synchronized GitSession getSingleton() {
    if (m_Singleton == null)
      m_Singleton = new GitSession();
    return m_Singleton;
  }
}
