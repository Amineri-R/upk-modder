package model.modtree;

import static model.modtree.ModContext.ModContextType.AFTER_HEX;
import static model.modtree.ModContext.ModContextType.BEFORE_HEX;
import static model.modtree.ModContext.ModContextType.FILE_HEADER;
import static model.modtree.ModContext.ModContextType.HEX_CODE;
import static model.modtree.ModContext.ModContextType.VALID_CODE;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.swing.event.DocumentEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.StyledDocument;

import model.modtree.ModContext.ModContextType;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import static org.junit.Assert.*;
//import parser.unrealhex.OperandTable;
//import org.junit.Ignore;

/**
 *
 * @author Amineri
 */


public class ModTreeTest
{
    

	public ModTreeTest() throws IOException
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
//		// initialize Operand Table for all tests that use it
//		OperandTableParser parser = new OperandTableParser(Paths.get("config/operand_data.ini"));
//		try {
//			parser.parseFile();
//		} catch(IOException ex) {
//			Logger.getLogger(ModTreeTest.class.getName()).log(Level.SEVERE, null, ex);
//		}
   }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }


	private class MyDE implements DocumentEvent {

		private String newline = "\n";
		String[] initString
				= {"This is an editable JTextPane, ", //regular
					"another ", //italic
					"styled ", //bold
					"text ", //small
					"component, ", //large
					"which supports embedded components..." + newline,//regular
					" " + newline, //button
					"...and embedded icons..." + newline, //regular
					" ", //icon
					newline + "JTextPane is a subclass of JEditorPane that "
					+ "uses a StyledEditorKit and StyledDocument, and provides "
					+ "cover methods for interacting with those objects."
				};

		String[] initStyles
				= {"regular", "italic", "bold", "small", "large",
					"regular", "button", "regular", "icon",
					"regular"
				};

		DefaultStyledDocument testdoc = new DefaultStyledDocument();

		public MyDE() {
			try {
				for(int i = 0; i < initString.length; i ++) {
					testdoc.insertString(testdoc.getLength(), initString[i],
							testdoc.getStyle(initStyles[i]));
				}
			} catch(BadLocationException ble) {
				System.err.println("Couldn't insert initial text into text pane.");
			}
		}

		@Override
		public int getOffset() {
			return 0;
		}

		@Override
		public int getLength() {
			return 4;
		}

		@Override
		public Document getDocument() {
			return testdoc;
		}

		@Override
		public DocumentEvent.EventType getType() {
			return DocumentEvent.EventType.INSERT;
		}

		@Override
		public DocumentEvent.ElementChange getChange(Element elmnt) {
			return null;
		}
	}


    /**
     * Test of getDefaultRootNode method, of class ModDocument.
     * @throws BadLocationException 
     */
    @Test
    public void testGetDefaultRootNode() throws BadLocationException
    {
        System.out.println("getDefaultRootNode");
        ModTree instance = new ModTree();
        ModTreeNode result = instance.getRoot();
        assertNotNull(result);
    }

	/**
     * Test of setLocalContext method, of class ModTreeNode.
     */
    @Test
    public void testSetAndInLocalContext()
    {
        System.out.println("setLocalContext");
        ModTreeNode instance = new ModTreeNode(null);
        instance.setContextFlag(HEX_CODE, true);
        assertTrue(instance.getContextFlag(HEX_CODE));
        instance.setContextFlag(HEX_CODE, false);
        assertFalse(instance.getContextFlag(HEX_CODE));
        
        instance.setContextFlag(VALID_CODE, true);
        assertTrue(instance.getContextFlag(VALID_CODE));
        instance.setContextFlag(VALID_CODE, false);
        assertFalse(instance.getContextFlag(VALID_CODE));
        
        instance.setContextFlag(BEFORE_HEX, true);
        assertTrue(instance.getContextFlag(BEFORE_HEX));
        instance.setContextFlag(BEFORE_HEX, false);
         assertFalse(instance.getContextFlag(BEFORE_HEX));

        instance.setContextFlag(AFTER_HEX, true);
        assertTrue(instance.getContextFlag(AFTER_HEX));
        instance.setContextFlag(AFTER_HEX, false);
        assertFalse(instance.getContextFlag(AFTER_HEX));

        instance.setContextFlag(FILE_HEADER, true);
        assertTrue(instance.getContextFlag(FILE_HEADER));
        instance.setContextFlag(FILE_HEADER, false);
        assertFalse(instance.getContextFlag(FILE_HEADER));
    }

    /**
     * Test of resetContextFlags method, of class ModTreeNode.
     * @throws BadLocationException 
     */
    @Test
    public void testResetContextFlags() throws BadLocationException
    {
        System.out.println("resetContextFlags");
        ModTreeRootNode instance = new ModTreeRootNode(null);
        instance.resetContextFlags();
        assertFalse(instance.getContextFlag(HEX_CODE));
        assertTrue(instance.getContextFlag(FILE_HEADER));
        
        ModTree d = new ModTree();
		ModTreeRootNode r = (ModTreeRootNode)d.getRoot();
        r.resetContextFlags();
        assertFalse(r.getContextFlag(HEX_CODE));
        assertTrue(r.getContextFlag(FILE_HEADER));
    }

    /**
     * Test of getDocument method, of class ModTreeNode.
     * @throws BadLocationException 
     */
    @Test
    public void testGetTree() throws BadLocationException
    {
        System.out.println("getTree");
        ModTreeNode instance = new ModTreeNode(null);
        ModTree expResult = null;
        ModTree result = instance.getTree();
        assertEquals(expResult, result);
		
		ModTree d = new ModTree();
		ModTreeRootNode r = (ModTreeRootNode)d.getRoot();
		ModTreeNode e = new ModTreeNode(r);
		ModTreeLeaf t = new ModTreeLeaf(e, "foo", true);
		assertEquals(d, t.getTree());
		assertEquals(d, e.getTree());
		assertEquals(d, r.getTree());
	}


    /**
     * Test of isCode method, of class ModTreeNode.
     */
    @Test
    public void testIsCode()
    {
        System.out.println("isCode");
        ModTreeNode instance = new ModTreeNode(null);
        boolean expResult = false;
        boolean result = instance.isCode();
        assertEquals(expResult, result);
    }

    /**
     * Test of parseUnrealHex method, of class ModTreeNode.
	 * @throws java.io.IOException
     */
    @Test
    public void testParseUnrealHex() throws IOException
    {
        System.out.println("parseUnrealHex");
		String in = "\t\t0F 00 34 D2 00 00 25 // comment\n";
		ModTreeNode e1 = new ModTreeNode(null, true);
		e1.setRange(0, 34);
		ModTreeLeaf t1 = new ModTreeLeaf(e1, in, true);
		e1.addNode(t1);
		t1.setRange(0, 34);
		e1.parseUnrealHex(null, 0);
		assertEquals(3, e1.getChildNodeCount()); // three children: "\t\t", "OperandTreeNode_0F", "// comment\n"
		assertEquals(3, e1.getChildAt(1).getChildCount());  // three childen: "0F ", "OperandTreeNode_00", "25 "
    }

	/**
	 * Test that contexts are set properly.
	 * @throws javax.swing.text.BadLocationException
	 */
	@Test
	public void testContextMaintanence() throws BadLocationException
	{
		System.out.println("file-level context test");
		ModTree d = new ModTree();
		ModTreeRootNode r = (ModTreeRootNode) d.getRoot();
		r.insertString(0, "MODFILEVERSION=1\n"
							+ "UPKFILE=a\n"
							+ "GUID=b\n"
							+ "FUNCTION=c\n"
							+ "[BEFORE_HEX]\n"
							+ "[CODE]\n"
							+ "//comment\n"
							+ "0B 0B 0B \n"  // <== should have HEX_CODE context
							+ "[/CODE]\n"
							+ "// comment\n"
							+ "[/BEGIN_HEX",null);
		assertEquals(1, r.getNodeCount());
		r.reorganizeAfterInsertion();
		assertEquals(11, r.getNodeCount());
		assertEquals("0B 0B 0B \n", r.getNode(7).getFullText());
		assertTrue(r.getNode(7).getContextFlag(HEX_CODE)); // this assertion is failing
		assertTrue(r.getNode(6).getContextFlag(BEFORE_HEX)); // this assertion also fails
		assertFalse(r.getNode(9).getContextFlag(HEX_CODE));
	}
	
    /**
     * Test of isValidHexLine method, of class ModTreeNode.
     */
    @Test
    public void testIsValidHexLine()
    {
        System.out.println("isValidHexLine");
        ModTreeLeaf t1 = new ModTreeLeaf(null, "    A7 00 00 FF // comment\n", true);
        assertTrue(t1.isValidHexLine());
        ModTreeLeaf t2 = new ModTreeLeaf(null, "  foobar  A7 00 00 FF // comment\n", true);
        assertFalse(t2.isValidHexLine());
        ModTreeLeaf t3 = new ModTreeLeaf(null, "    A7  00 00 FF // comment\n", true);
        assertFalse(t3.isValidHexLine());
    }

    /**
     * Test of toHexStringArray method, of class ModTreeNode.
     */
    @Test
    public void testToHexStringArray()
    {
        System.out.println("toHexStringArray");
        ModTreeRootNode r = new ModTreeRootNode(null);
        ModTreeNode n = new ModTreeNode(r);
        ModTreeLeaf instance = new ModTreeLeaf(n, "     A7 57 BB FF //a comment\n");
        String[] expResult = {"     ", "A7 57 BB FF ", "//a comment\n"};
        String[] result = instance.toHexStringArray();
        assertArrayEquals(expResult, result);
        ModTreeLeaf instance2 = new ModTreeLeaf(n, "  foo     A7 57 BB FF //a comment\n");
        String[] expResult2 = null;
        String[] result2 = instance2.toHexStringArray();
        assertArrayEquals(expResult2, result2);
    }

    /**
     * Test of addNode method, of class ModTreeNode.
     */
    @Test
    public void testAddNode_ModTreeNode()
    {
        System.out.println("addNode");
        ModTreeNode e = new ModTreeNode(null);
        ModTreeNode instance = new ModTreeNode(null);
        instance.addNode(e);
    }

    /**
     * Test of addNode method, of class ModTreeNode.
     */
    @Test
    public void testAddNode_int_ModTreeNode()
    {
        System.out.println("addNode");
        int index = 0;
        ModTreeNode e = null;
        ModTreeNode instance = new ModTreeNode(null);
        instance.addNode(index, e);
    }

    /**
     * Test of updateContexts method, of class ModTreeNode.
     * @throws BadLocationException 
     */
    @Test
    public void testUpdateContexts() throws BadLocationException
    {
        System.out.println("updateContexts");
        ModTree d = new ModTree();
        ModTreeRootNode r = (ModTreeRootNode) d.getRoot();
        ModTreeNode e2 = new ModTreeNode(r);
        r.resetContextFlags();
        assertFalse("HEX_CODE true, s.b. false", r.getContextFlag(HEX_CODE));
        assertTrue("FILE_HEADER false, s.b. true", r.getContextFlag(FILE_HEADER));
    }

    /**
     * Test of foundHeader method, of class ModTreeNode.
     */
    @Test
    public void testFoundHeader()
    {
        System.out.println("foundHeader");
        ModTreeNode instance = new ModTreeNode(null);
        boolean expResult = false;
        boolean result = instance.foundHeader();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTag method, of class ModTreeNode.
     */
    @Test
    public void testGetTagValue()
    {
        System.out.println("getTag");
        ModTreeNode instance = new ModTreeNode(null);
        String expResult = "bar";
        String result = instance.getTagValue("foo=bar");
        assertEquals(expResult, result);
    }

    /**
     * Test of remove method, of class ModTreeNode.
     * @throws BadLocationException 
     */
    @Test
    public void testRemove() throws BadLocationException
    {
        System.out.println("remove");
        int offset = 0;
        int length = 0;
        ModTreeNode instance = new ModTreeNode(null);
        instance.remove(offset, length);
		ModTree t = new ModTree();
		ModTreeRootNode r = new ModTreeRootNode(t);
		String in = "123456789\n"
				+ "123456789\n"
				+ "123456789\n"
				+ "1234567890";
		r.insertString(offset, in, null);
		r.reorganizeAfterInsertion();
		assertEquals(40, r.getEndOffset());
		r.remove(5, 8);
		r.reorganizeAfterDeletion();
		String expResult = "12345456789\n"
				+ "123456789\n"
				+ "1234567890";
		assertEquals(expResult, r.getFullText());
		assertEquals(0, r.getStartOffset());
		assertEquals(32, r.getEndOffset());
		assertEquals(0, r.getChildNodeAt(0).getStartOffset());
		assertEquals(12, r.getChildNodeAt(0).getEndOffset());
		assertEquals(12, r.getChildNodeAt(1).getStartOffset());
		assertEquals(22, r.getChildNodeAt(1).getEndOffset());
		assertEquals(22, r.getChildNodeAt(2).getStartOffset());
		assertEquals(32, r.getChildNodeAt(2).getEndOffset());
		assertEquals(0, r.getChildNodeAt(0).getChildNodeAt(0).getStartOffset());
		assertEquals(12, r.getChildNodeAt(0).getChildNodeAt(0).getEndOffset());
		assertEquals(12, r.getChildNodeAt(1).getChildNodeAt(0).getStartOffset());
		assertEquals(22, r.getChildNodeAt(1).getChildNodeAt(0).getEndOffset());
		assertEquals(22, r.getChildNodeAt(2).getChildNodeAt(0).getStartOffset());
		assertEquals(32, r.getChildNodeAt(2).getChildNodeAt(0).getEndOffset());
	}

    /**
     * Test of removeModTreeNode method, of class ModTreeNode.
     */
    @Test
    public void testRemoveModNode()
    {
        System.out.println("removeModTreeNode");
        ModTreeRootNode r = new ModTreeRootNode(null);
        ModTreeNode e1 = new ModTreeNode(r);
        ModTreeNode e2 = new ModTreeNode(e1);
        ModTreeNode e3 = new ModTreeNode(e1);
        e1.addNode(e2);
        e1.addNode(e3);
        e2.removeModNode();
        assertEquals(e3, e1.getChildNodeAt(0));
    }

    /**
     * Test of insertString method, of class ModTreeNode.
     */
    @Test
    public void testInsertString()
    {
        System.out.println("insertString");
        int offset = 0;
        String string = "";
        AttributeSet as = null;
        ModTreeNode instance = new ModTreeNode(null);
        instance.insertString(offset, string, as);
    }

    /**
     * Test of insertStringAtLeaf method, of class ModTreeNode.
     */
    @Test
    public void testInsertStringAtLeaf()
    {
        System.out.println("insertStringAtLeaf");
        int offset = 0;
        String string = "";
        AttributeSet as = null;
        ModTreeNode e = new ModTreeNode(null);
		e.setRange(0, 4);
		ModTreeLeaf t = new ModTreeLeaf(e, "test", true);
		t.setRange(0, 4);
        t.insertStringAtLeaf(2, "foo", as);
		String result = t.getFullText();
		String expResult = "tefoost";
		assertEquals(expResult, result);
    }

    /**
     * Test of setString method, of class ModTreeNode.
     */
    @Test
    public void testSetString()
    {
        System.out.println("setString");
        String s = "foo";
		ModTreeNode node = new ModTreeNode(null, true);
        ModTreeLeaf leaf = new ModTreeLeaf(node, s, true);
		String result = leaf.getText();
		String expResult = "foo";
		assertEquals(expResult, result);
    }

    /**
     * Test of getString method, of class ModTreeNode.
     */
    @Test
    public void testGetString()
    {
        System.out.println("getString");
        ModTreeNode e = new ModTreeNode(null);
		ModTreeLeaf t = new ModTreeLeaf(e, "foobar", true);
		String expResult = "foobar";
		String result = t.getText();
		assertEquals(expResult, result);
//        expResult = "";
//        result = e.getString();
//        assertEquals(expResult, result);
    }
    
    /**
     * Test of getLineParent method, of class ModTreeNode.
     */
    @Test
    public void testGetLineParent()
    {
        System.out.println("getLineParent");
		ModTreeRootNode r = new ModTreeRootNode(null);
        ModTreeNode e = new ModTreeNode(r);
        ModTreeLeaf t = new ModTreeLeaf(e, "test", true);
        ModTreeNode expResult = e;
        ModTreeNode result = t.getLineParent();
		assertEquals(expResult, result);
		r = new ModTreeRootNode(null);
        e = new ModTreeNode(r);
        ModOperandNode oe1 = new ModOperandNode(e);
        ModOperandNode oe2 = new ModOperandNode(oe1);
        ModReferenceLeaf rt = new ModReferenceLeaf(oe2, true);
        assertEquals(e, oe1.getLineParent());
        assertEquals(e, oe2.getLineParent());
        assertEquals(e, rt.getLineParent());
    }

    /**
     * Test of toString method, of class ModTreeNode.
     */
    @Test
    public void testToString()
    {
        System.out.println("toString");
        ModTreeNode e = new ModTreeNode(null);
		ModTreeLeaf t1 = new ModTreeLeaf(e, "foo", true);
		e.addNode(t1);
		ModTreeLeaf t2 = new ModTreeLeaf(e, "bar", true);
		e.addNode(t2);
        String expResult = "foobar";
        String result = e.getFullText();
        assertEquals(expResult, result);
    }

    /**
     * Test of getText method, of class ModTreeNode.
	 * @throws javax.swing.text.BadLocationException
     */
    @Test
    public void testGetText_int_int() throws BadLocationException
    {
        System.out.println("getText");
        int offset = 0;
        int length = 0;
        ModTreeNode instance = new ModTreeNode(null);
        String expResult = "";
        String result = instance.getText(offset, length);
        assertEquals(expResult, result);
		ModTree d = new ModTree();
		ModTreeRootNode r = (ModTreeRootNode) d.getRoot();
		r.setRange(0, 19);
		ModTreeNode e1 = new ModTreeNode(r, true);
		r.addNode(e1);
		e1.setRange(0, 9);
		ModTreeLeaf t1 = new ModTreeLeaf(e1, "testing1\n", true);
		e1.addNode(t1);
		t1.setRange(0, 9);
		ModTreeNode e2 = new ModTreeNode(r, true);
		r.addNode(e2);
		e2.setRange(9, 19);
		ModTreeLeaf t2 = new ModTreeLeaf(e2, "testing2\n", true);
		e2.addNode(t2);
		t2.setRange(9, 19);
		String expResult2 = "g1\nte";
//		String result2 = d.getText(6, 5);
//		assertEquals(expResult2, result2);
    }

    /**
     * Test of getParentNode method, of class ModTreeNode.
     */
    @Test
    public void testGetParentNode()
    {
        System.out.println("getParentNode");
        ModTreeNode instance = new ModTreeNode(null);
        ModTreeNode expResult = null;
        ModTreeNode result = instance.getParentNode();
        assertEquals(expResult, result);
    }

    /**
     * Test of isLeaf method, of class ModTreeNode.
     */
    @Test
    public void testIsLeaf()
    {
        System.out.println("isLeaf");
        ModTreeNode instance = new ModTreeNode(null);
        boolean expResult = false;
        boolean result = instance.isLeaf();
        assertEquals(expResult, result);
    }

    /**
     * Test of getName method, of class ModTreeNode.
     */
    @Test
    public void testGetName()
    {
        System.out.println("getName");
        ModTreeNode instance = new ModTreeNode(null);
        String expResult = "ModTreeNode";
        String result = instance.getName();
        assertEquals(expResult, result);
        ModOperandNode instance2 = new ModOperandNode(instance);
        String result2 = instance2.getName();
        String expResult2 = "ModOperandNode_";
        assertEquals(expResult2, result2);
    }

    /**
     * Test of getAttributes method, of class ModTreeNode.
     */
    @Test
    public void testGetAttributes()
    {
        System.out.println("getAttributes");
        ModTreeNode instance = new ModTreeNode(null);
        AttributeSet expResult = null;
        AttributeSet result = instance.getAttributes();
        assertEquals(expResult, result);
    }

    /**
     * Test of getStartOffset method, of class ModTreeNode.
     */
    @Test
    public void testGetStartOffset()
    {
        System.out.println("getStartOffset");
        ModTreeNode instance = new ModTreeNode(null);
        int expResult = 0;
        int result = instance.getStartOffset();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEndOffset method, of class ModTreeNode.
     */
    @Test
    public void testGetEndOffset()
    {
        System.out.println("getEndOffset");
        ModTreeNode instance = new ModTreeNode(null);
        int expResult = 0;
        int result = instance.getEndOffset();
        assertEquals(expResult, result);
    }

    /**
     * Test of getNodeIndex method, of class ModTreeNode.
	 * @throws javax.swing.text.BadLocationException
     */
    @Test
    public void testGetNodeIndex() throws BadLocationException
    {
        System.out.println("getNodeIndex");
        ModTree d = new ModTree();
        ModTreeRootNode r = (ModTreeRootNode) d.getRoot();
        r.insertString(0, "foo\nbar\nstuff", null);
        r.reorganizeAfterInsertion();
        assertEquals(0, r.getNodeIndex(-1));
        assertEquals(0, r.getNodeIndex(3));
        assertEquals(1, r.getNodeIndex(5));
        assertEquals(2, r.getNodeIndex(8));
        assertEquals(2, r.getNodeIndex(400));
    }

    /**
     * Test of getNodeCount method, of class ModTreeNode.
     */
    @Test
    public void testGetNodeCount()
    {
        System.out.println("getNodeCount");
        ModTreeNode instance = new ModTreeNode(null);
        int expResult = 0;
        int result = instance.getNodeCount();
        assertEquals(expResult, result);
    }

    /**
     * Test of getNode method, of class ModTreeNode.
     */
    @Test
    public void testGetNode()
    {
        System.out.println("getNode");
        int n = 0;
        ModTreeNode instance = new ModTreeNode(null);
        ModTreeNode expResult = null;
        ModTreeNode result = instance.getNode(n);
        assertEquals(expResult, result);
    }

    /**
     * Test of getContextFlag method, of class ModTreeNode.
     * @throws BadLocationException 
     */
    @Test
    public void testSetAndInContext() throws BadLocationException
    {
        System.out.println("getContextFlag");
        ModTree d = new ModTree();
        ModTreeRootNode r = (ModTreeRootNode)d.getRoot();
        ModTreeNode e = new ModTreeNode(d.getRoot());
        assertFalse(e.getContextFlag(HEX_CODE));
    }

    /**
     * Test of setContext method, of class ModTreeNode.
     */
    @Test
    public void testSetContextFlag()
    {
        System.out.println("setContext");
        ModContextType type = null;
        boolean val = false;
        ModTreeNode instance = new ModTreeNode(null);
        instance.setContextFlag(type, val);
    }

    /**
     * Test of getMemorySize method, of class ModTreeNode.
     * @throws BadLocationException 
     */
    @Test
    public void testGetMemorySize() throws BadLocationException
    {
        System.out.println("getMemorySize");
        ModTreeNode instance = new ModTreeNode(null);
        int expResult = 0;
        int result = instance.getMemorySize();
        assertEquals(expResult, result);
		
		// comprehensive test
		String in = "ModFileVersion=5\n"
				+ "UPKFILE=a\n"
				+ "GUID=c\n"
				+ "FUNCTION=d\n"
				+ "[CODE]\n"
				+ "0F 00 24 A1 00 00 1B 32 43 00 00 00 00 00 00 01 32 FF 00 00 16 // test hex\n"
				+ "[/CODE]";
		ModTree t = new ModTree();
		ModTreeRootNode r = new ModTreeRootNode(t);
		r.insertString(0, in, null);
		r.reorganizeAfterInsertion();
		assertEquals(29 , r.getMemorySize());
    }

    /**
     * Test of isVFFunctionRef method, of class ModTreeNode.
     */
    @Test
    public void testIsVFFunctionRef()
    {
        System.out.println("isVFFunctionRef");
        ModTreeNode n1 = new ModTreeNode(null);
        boolean result = n1.isVirtualFunctionRef();
        assertEquals(false, result);
		ModReferenceLeaf n2 = new ModReferenceLeaf(n1,false);
		assertEquals(false, n2.isVirtualFunctionRef());
		ModReferenceLeaf n3 = new ModReferenceLeaf(n1,true);
		assertEquals(true, n3.isVirtualFunctionRef());
    }

    /**
     * Test of getOffset method, of class ModTreeNode.
     */
    @Test
    public void testGetOffset()
    {
        System.out.println("getOffset");
        ModTreeNode instance = new ModTreeNode(null);
        int expResult = -1;
        int result = instance.getOffset();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRefValue method, of class ModTreeNode.
     */
    @Test
    public void testGetRefValue()
    {
        System.out.println("getRefValue");
        ModTreeNode instance = new ModTreeNode(null);
        int expResult = -1;
        int result = instance.getRefValue();
        assertEquals(expResult, result);
    }

	/**
	 * Test of setDocument method, of class ModTree.
	 */
//	@Test
//	public void testSetDocument() throws BadLocationException {
//		System.out.println("setDocument");
//		StyledDocument d = new DefaultStyledDocument();
//		ModTree r = new ModTree();
//		r.setDocument(d);
//		assertEquals(d, r.getDocument());
//		assertEquals(d, r.doc);
//	}

	/**
	 * Test of getDocument method, of class ModTree.
	 * Verifies null case does not crash app.
	 * @throws BadLocationException 
	 */
	@Test
	public void testGetDocument() throws BadLocationException {
		System.out.println("getDocument");
		ModTree instance = new ModTree();
		StyledDocument result = (StyledDocument) instance.getDocument();
		assertNotNull(result);
	}

	/**
	 * Test of getFileVersion method, of class ModTree.
	 * Tests un-initialized case.
	 * @throws BadLocationException 
	 */
	@Test
	public void testGetFileVersion() throws BadLocationException {
		System.out.println("getFileVersion");
		ModTree instance = new ModTree();
		int expResult = -1;
		int result = instance.getFileVersion();
		assertEquals(expResult, result);
	}

	/**
	 * Test of setFileVersion method, of class ModTree.
	 * @throws BadLocationException 
	 */
	@Test
	public void testSetFileVersion() throws BadLocationException {
		System.out.println("setFileVersion");
		int fileVersion = 27;
		ModTree t = new ModTree();
		t.setFileVersion(fileVersion);
		assertEquals(fileVersion, t.getFileVersion());
	}

	/**
	 * Test of getUpkName method, of class ModTree.
	 * @throws BadLocationException 
	 */
	@Test
	public void testGetUpkName() throws BadLocationException {
		System.out.println("getUpkName");
		ModTree instance = new ModTree();
		String expResult = "";
		String result = instance.getUpkName();
		assertEquals(expResult, result);
	}

	/**
	 * Test of setUpkName method, of class ModTree.
	 * @throws BadLocationException 
	 */
	@Test
	public void testSetUpkName() throws BadLocationException {
		System.out.println("setUpkName");
		String upkName = "testname.upk";
		ModTree t = new ModTree();
		t.setUpkName(upkName);
		assertEquals(upkName, t.getUpkName());
	}

	/**
	 * Test of getGuid method, of class ModTree.
	 * @throws BadLocationException 
	 */
	@Test
	public void testGetGuid() throws BadLocationException {
		System.out.println("getGuid");
		ModTree instance = new ModTree();
		String expResult = "";
		String result = instance.getGuid();
		assertEquals(expResult, result);
	}

	/**
	 * Test of setGuid method, of class ModTree.
	 * @throws BadLocationException 
	 */
	@Test
	public void testSetGuid() throws BadLocationException {
		System.out.println("setGuid");
		String guid = "11 22 33 AA 87 ";
		ModTree t = new ModTree();
		t.setGuid(guid);
		assertEquals(guid, t.getGuid());
	}

	/**
	 * Test of getFunctionName method, of class ModTree.
	 * @throws BadLocationException 
	 */
	@Test
	public void testGetFunctionName() throws BadLocationException {
		System.out.println("getFunctionName");
		ModTree instance = new ModTree();
		String expResult = "";
		String result = instance.getFunctionName();
		assertEquals(expResult, result);
	}

	/**
	 * Test of setFunctionName method, of class ModTree.
	 * @throws BadLocationException 
	 */
	@Test
	public void testSetFunctionName() throws BadLocationException {
		System.out.println("setFunctionName");
		String functionName = "function@class";
		ModTree t = new ModTree();
		t.setFunctionName(functionName);
		assertEquals(functionName, t.getFunctionName());
	}

	/**
	 * Test of processNextEvent method, of class ModTree.
	 * @throws BadLocationException 
	 */
	@Test
	public void testProcessNextEvent() throws BadLocationException {
		System.out.println("processNextEvent");
		// null case -- verify code does not crash
		ModTree instance = new ModTree();
		instance.processNextEvent();

		// full case
		DocumentEvent de = new MyDE();
		ModTree t = new ModTree();
		t.docEvents.add(de);
		t.processNextEvent();
		assertEquals("This", t.getRoot().getFullText());
		assertEquals(0, t.getRoot().getStartOffset());
		assertEquals(4, t.getRoot().getEndOffset());
		assertEquals(0, t.getRoot().getChildNodeAt(0).getStartOffset());
		assertEquals(4, t.getRoot().getChildNodeAt(0).getEndOffset());
		assertEquals(0, t.getRoot().getChildNodeAt(0).getChildNodeAt(0).getStartOffset());
		assertEquals(4, t.getRoot().getChildNodeAt(0).getChildNodeAt(0).getEndOffset());
	}

	/**
	 * Test of processDocumentEvent method, of class ModTree.
	 */
	@Test
	public void testProcessDocumentEvent() throws Exception {
		System.out.println("processDocumentEvent");
		// null case -- verify code does not crash
		DocumentEvent de = null;
		ModTree t = new ModTree();
		t.processDocumentEvent(de);

		//full case
		de = new MyDE();
		t.processDocumentEvent(de);
		assertEquals("This", t.getRoot().getFullText());
		assertEquals(0, t.getRoot().getStartOffset());
		assertEquals(4, t.getRoot().getEndOffset());
		assertEquals(0, t.getRoot().getChildNodeAt(0).getStartOffset());
		assertEquals(4, t.getRoot().getChildNodeAt(0).getEndOffset());
		assertEquals(0, t.getRoot().getChildNodeAt(0).getChildNodeAt(0).getStartOffset());
		assertEquals(4, t.getRoot().getChildNodeAt(0).getChildNodeAt(0).getEndOffset());
	}
    
}
