/*
 * Copyright (C) 2013 Rachel Norman
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package parser.unrealhex;


/**
 *
 * @author Amineri
 */
public class ReferenceParser 
{
    String m_sInputString;
    String[] m_sTokens;
    String m_sOutputString;
    
    private int m_iInputPosition;
    
    

    public ReferenceParser() //OperandTable kOpTable)
    {
//        m_kOpTable = kOpTable;
    }
    
	public String parseString(String sHex) {
		m_sInputString=sHex;
		m_sTokens=m_sInputString.split("\\s");
		m_iInputPosition=0;
		m_sOutputString="";

		while (m_iInputPosition < m_sTokens.length) {
			parseGenericObject();
		}
		return m_sOutputString;
	}
    
	private void parseGenericObject() {
		String sOpCodes=OperandTable.getOperandString(m_sTokens[m_iInputPosition]);
		if(!m_sTokens[m_iInputPosition].equalsIgnoreCase(sOpCodes.split("\\s", 2)[0])) {
			System.out.println("/* opcode mismatch */");
		} else {
			String[] sParseItems=sOpCodes.split("\\s", 2)[1].split("\\s");
			for(String sParseItem : sParseItems) {
				sParseItem=sParseItem.toUpperCase();
				if(sParseItem.matches("[0-9]")) {
					mirrorTokens(Integer.parseInt(sParseItem));
					continue;
				}
				if(sParseItem.equals("G")) {
					parseGenericObject();
					continue;
				}
				if(sParseItem.equals("P")) {
					while(!m_sTokens[m_iInputPosition].equals("16")) {
						parseGenericObject();
					}
					continue;
				}
				if(sParseItem.equals("R")) {
					tagReference();
					continue;
				}
				if(sParseItem.equals("NR")) {
					tagVFReference();
					continue;
				}
				if(sParseItem.equals("N")) {
					while(!m_sTokens[m_iInputPosition].equals("00")) {
						mirrorTokens(1);
					}
					mirrorTokens(1);
					continue;
				}
				if(sParseItem.startsWith("S")||sParseItem.equals("J")) {
					mirrorTokens(2);
				}
				if(sParseItem.equals("C")) {
					if(m_sTokens[m_iInputPosition].equalsIgnoreCase("FF")&&m_sTokens[m_iInputPosition+1].equalsIgnoreCase("FF")) {
						mirrorTokens(2);
					} else {
						mirrorTokens(2);
						parseGenericObject();
					}
				}
			}
		}
	}

    public void mirrorTokens(int iNum) {
		for(int I=0; I<iNum; I++) {
			m_sOutputString+=(m_sTokens[m_iInputPosition]+" ");
			m_iInputPosition++;
		}
	}
    
    public void tagReference() {
		int iReference=0;
		for(int I=0; I<4; I++) {
			iReference+=Integer.parseInt(m_sTokens[m_iInputPosition+I], 16)<<(8*I);
		}
		if(iReference!=0) {
			m_sOutputString+="{{ ";
			mirrorTokens(4);
			m_sOutputString+="}} ";
		} else {
			mirrorTokens(4);
		}
	}
    
    public void tagVFReference() {
		int iReference=0;
		for(int I=0; I<4; I++) {
			iReference+=Integer.parseInt(m_sTokens[m_iInputPosition+I], 16)<<(8*I);
		}
		if(iReference!=0) {
			m_sOutputString+="<< ";
			mirrorTokens(4);
			m_sOutputString+=">> ";
		} else {
			mirrorTokens(4);
		}
	}

}
