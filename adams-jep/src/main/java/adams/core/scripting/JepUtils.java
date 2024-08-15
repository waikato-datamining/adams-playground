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
 * JepUtils.java
 * Copyright (C) 2024 University of Waikato, Hamilton, New Zealand
 */

package adams.core.scripting;

import jep.JepConfig;
import jep.SharedInterpreter;

/**
 * Helper methods around Jep.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class JepUtils {

  /** the global configuration for the shared interpreters. */
  protected static JepConfig m_SharedInterpreterConfig;

  /**
   * Returns an instance of a shared interpreter. Ensure
   *
   * @return		the instance
   */
  public static synchronized SharedInterpreter getSharedInterpreter() {
    if (m_SharedInterpreterConfig == null) {
      m_SharedInterpreterConfig = new JepConfig();
      // ensure that Python's stdout/stderr are printed in IDE
      m_SharedInterpreterConfig.redirectStdout(System.out);
      m_SharedInterpreterConfig.redirectStdErr(System.err);
      // set global config
      SharedInterpreter.setConfig(m_SharedInterpreterConfig);
    }
    return new SharedInterpreter();
  }
}
