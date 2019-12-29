/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bst_gui;

/**
 *
 * @author siddh
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;
import javax.swing.border.*;
import javax.swing.BorderFactory;
import java.util.*;

public class BST_GUI extends JFrame implements ActionListener {

    /**
     * @param args the command line arguments
     */
    
    public static class Node{
        public int val=0;
        public int x;
        public int y;
    }
    
    
    public static final int maxNodes = 1025;
    public static Node[] BST = new Node[maxNodes];
    public int nv = 0;
    
    public static int frameWidth = 1000;
    public static int frameHeight = 1000;
    public final int size = 50;
    public int animationSpeed = 1000;
    
    
    public final int nodeTextOffset = (int)(4.5*(float)size/5);
    public boolean hasBeenResized = false;
    
    public int changingXOffset = 0;
    public int changingDepth = 0;
    public int lastInsertedPos = -1;
    public String bufferString = "";
    
    //all declarations for the components are global so they may be accessible
    
    JPanel mainPanel = new JPanel(new GridLayout(2 , 1));
    
    JPanel drawingBoard = new JPanel();
    
    //divided up into a text area on the left, and button space on the right
    JPanel getInputHere = new JPanel(new GridLayout(1 , 2));
    
    //the panel is divided into two parts , the inputs on top, the output on bottom
    JPanel inputOutputTextSpace = new JPanel(new GridLayout(2 , 1));
    
    //The textAreas are underneath
    JTextArea inputGoesHere = new JTextArea();
    JTextArea outputGoesHere = new JTextArea();
    
    //one for add, one for delete, one for find 
    JPanel buttonSpaceHere = new JPanel(new GridLayout(2 , 2));
    
    //button to add to bst
    JButton addTheseToBST = new JButton("Add");

    //button to delete all entries in textBox 
    JButton deleteTheseFromBST = new JButton("Delete");
    
    //button to find all following numbers
    JButton findTheseInBST = new JButton("Find");
    
    //button to enter custom command
    JButton cmd = new JButton("cmd");
    
    
    BST_GUI()
    {      
        //1000x1000 size
        setSize(frameWidth , frameHeight);
        
        //title is BST with GUI
        setTitle("Java Final Project : BST with GUI !");
        
        //following two lines make the frame open in center of the screen, less annoying
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        
        //add the mainPanel to the frame
        add(mainPanel);
        
        
        //Now adding the drawing board on top
        mainPanel.add(drawingBoard);
        
        //and the input space at the bottom
        mainPanel.add(getInputHere);
        
        getInputHere.add(inputOutputTextSpace);
        getInputHere.add(buttonSpaceHere);
        
        //inputOutputSpace
        
        inputOutputTextSpace.add(inputGoesHere);
        inputOutputTextSpace.add(outputGoesHere);
        
        //creating an empty border for textAreas to add padding
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        inputGoesHere.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        outputGoesHere.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        
        
        
        
        //buttonSpace
        
        //add
        buttonSpaceHere.add(addTheseToBST);
        addTheseToBST.addActionListener(this);
        
        //delete
        buttonSpaceHere.add(deleteTheseFromBST);
        deleteTheseFromBST.addActionListener(this);
        
        //find
        buttonSpaceHere.add(findTheseInBST);
        findTheseInBST.addActionListener(this);
        
        //cmd
        buttonSpaceHere.add(cmd);
        cmd.addActionListener(this);

        
        //stop the program on window close
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //make the frame visible
        setVisible(true);
        

        
        
    }

    public static void main(String[] args) {
        
        
        // first instantiate all nodes
        for(int i=0;i<maxNodes;i++) BST[i] = new Node();
        
        new BST_GUI();
        
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        
        Graphics g = drawingBoard.getGraphics();
        String itxt = inputGoesHere.getText();
        Scanner scan = new Scanner(itxt);
            
        
        if( ae.getSource() == addTheseToBST )
        {
           
            while(scan.hasNext())
            {
                int rX = frameWidth/2;
                int rY = 0;
                int toInsert = scan.nextInt();
                
                changingXOffset=0;
                changingDepth=0;
                
                insertNode(1 , toInsert);
                
                int curX = rX + changingXOffset;
                int curY = rY + 100*changingDepth;
                
                drawNode(g , curX , curY , Integer.toString(toInsert));
                
                //1 second delay in displaying the nodes
                try
                {
                    Thread.sleep(animationSpeed);
                }
                catch(InterruptedException ex)
                {
                    Thread.currentThread().interrupt();
                }
            }
            hasBeenResized = false;
            outputGoesHere.setText(TraverseTree(1));
  
        }
        else if( ae.getSource() == findTheseInBST )
        {
            
            
            bufferString = "";
            
            while(scan.hasNext())
            {
                BinarySearch(1 , scan.nextInt());
            }
            
            outputGoesHere.setText(bufferString);
            bufferString = "";
        }
        else if(ae.getSource() == deleteTheseFromBST)
        {
            
            while(scan.hasNext())
            {
                deleteNode(1 , scan.nextInt());
            }
//            outputGoesHere.setText("i was pressed");
        }
        else if( ae.getSource() == cmd )
        {
            
            String command = inputGoesHere.getText();
            
            switch(command)
            {
                case "in-order":
                    outputGoesHere.setText(TraverseTree(1));
                    break;
                case "level-order":
                    outputGoesHere.setText(BFSPATH(1));
                    break;
                case "height":
                    outputGoesHere.setText(Integer.toString(height(1) + 1));
                    break;
                case "pre-order":
                    outputGoesHere.setText(PreOrder(1));
                    break;
                case "post-order":
                    outputGoesHere.setText(PostOrder(1));
                    break;
                case "as-fast":
                    animationSpeed=250;
                    outputGoesHere.setText("fast animation speed enabled");
                    break;
                case "as-normal":
                    animationSpeed = 1000;
                    outputGoesHere.setText("normal animation speed enabled");
                    break;
                case "as-none":
                    animationSpeed = 0;
                    outputGoesHere.setText("animation disabled");
                    break;
                case "vertices":
                    nv=0;
                    BFSPATH(1);
                    outputGoesHere.setText(Integer.toString(nv));
                    break;
                default:
                    outputGoesHere.setText("That is not a valid command");
                    break;
            }
            
            
            
        }
        
    }
    
    
    public int height(int i)
    {
        if(BST[i].val == 0) return -1;
        
        return Math.max(height(left(i)) , height(right(i))) + 1;
        
    }
    
    
    
    
    
    public String returnSomeString(String s)
    {
        return s;
    }
    
    
    public int left(int i)
    {
        return 2*i;
    }
    
    public int right(int i)
    {
        return 2*i+1;
    }
    
    public int parent(int i)
    {
        if(i == 1) return i;
        
        return (i/2);      
    }
    
    
    
    public String PostOrder(int i)
    {
        if(BST[i].val == 0) return "";
        
        String output = "";
        
        
        output += PostOrder(left(i));
        output += PostOrder(right(i));
        output += BST[i].val + " ";
        
        return output;
    }
    
    public String PreOrder(int i)
    {
        if(BST[i].val == 0) return "";
        
        String output = "";
        
        output += BST[i].val + " ";
        output += PreOrder(left(i));
        output += PreOrder(right(i));
        
        return output;
        
    }
    
    
    public String TraverseTree(int i){
        
        Graphics g = drawingBoard.getGraphics();
        
        if(BST[i].val == 0)
        {
            return "";
        }
        
        
        // in-order traversal 
        
        String output = "";
        output += TraverseTree(left(i));
        output += " " + Integer.toString(BST[i].val) + " ";   
        output += TraverseTree(right(i));
        
        
        return output;      
    }
    
    
    public String BFSPATH(int i)
    {
        String p = "";
        
        Queue<Integer> q = new LinkedList<>();
        
        q.add(i);
        
        while(!q.isEmpty())
        {
            nv++;
            p += BST[q.peek()].val + " ";
            
            if( !isNull(left(q.peek())) ) q.add(left(q.peek()));
            if( !isNull(right(q.peek())) ) q.add(right(q.peek()));
            
            q.remove(q.peek());
            
            
            
        }
        
        return p;
    
    }
    
    
    public boolean isNull(int i)
    {
        return (BST[i].val==0);
    }
    
    public void deleteTree(int i)
    {
        
        Queue<Integer> nextNode = new LinkedList<>();
        nextNode.add(i);
        
        
        while(!nextNode.isEmpty())
        {
            if( !isNull(left(nextNode.peek())) ) nextNode.add(left(nextNode.peek()));
            if( !isNull(right(nextNode.peek())) ) nextNode.add(right(nextNode.peek()));
            popLeaf(nextNode.peek());
            nextNode.remove(nextNode.peek());
        }
        
    }
    
   
    
   
    
    
    
    public void deleteNode(int i , int j)
    {
        
        //delete j
        
        if(BST[i].val == 0) return; //node i am at is null so just return, nothing to be done here
        
            
        if( BST[i].val < j )
        {
            blip(i , Color.MAGENTA);
            deleteNode( right(i) , j );
        }
        else if(BST[i].val > j)
        {
            blip(i , Color.MAGENTA);
            deleteNode( left(i) , j );
        }
        else if(BST[i].val == j)
        {
            nv--;
            blip(i , Color.MAGENTA);
            blip(i , Color.BLUE);
            if( BST[left(i)].val == 0 && BST[right(i)].val == 0 )
            {
                //node is a leaf
                popLeaf(i);
            }
            else 
            {
                
                
                
                //is a line node
                
                String insertAgain;
                int save = left(i);
                if( isNull(left(i)) && !isNull(right(i)) )
                {
                    
                    insertAgain = BFSPATH(right(i));
                    
                }
                else if( !isNull(left(i)) && isNull(right(i)) )
                {
                    
                    insertAgain = BFSPATH(left(i));
                }
                else
                {
                    
                    //take the left node's right most node, paste it onto i , then do bfs on the left node of right most left node of i and insert into the tree again
                    
                    
                    
                    
                    while(!isNull(right(save))){
                        
                        blip(save , Color.orange);
                        save = right(save);
                        
                    }
                    blip(save , Color.orange);
                    blip(save , Color.GREEN);
                    
                    BST[i].val = BST[save].val;
                    lastInsertedPos = -1;
                    drawNode(i);
                    if(!isNull(left(save)))insertAgain = BFSPATH(left(save));
                    else insertAgain = "";
                    
                }
                
                Scanner tscan;
                tscan = new Scanner(insertAgain);
                outputGoesHere.setText(insertAgain);
                if( (isNull(left(i)) && !isNull(right(i))) || ( !isNull(left(i)) && isNull(right(i)) ) )  deleteTree(i);
                else deleteTree(save);
                hasBeenResized=false;
                Graphics g = drawingBoard.getGraphics();
                
                while( tscan.hasNext() )
                {
                    
                    int rX = frameWidth/2;
                    int rY = 0;
                    int toInsert = tscan.nextInt();

                    changingXOffset=0;
                    changingDepth=0;
                    
                    insertNode( 1 ,  toInsert, false ); 
                    
                    int curX = rX + changingXOffset;
                    int curY = rY + 100*changingDepth;
                    
                    
                    connectNodes( parent(lastInsertedPos) , lastInsertedPos );
                    
                    lastInsertedPos = -1;
                    drawNode(g , curX , curY , Integer.toString(toInsert));
                    
        
                }
                hasBeenResized = false;
                 
                
                
                
                
                
            }
        }
    }
    
    
    
    public void BinarySearch(int i , int j)
    {
        if(BST[i].val == 0)
        {
            if(i == 1)
            {
                blip(i , Color.red);
            }
            else
            {
                blip(parent(i) , Color.red);
            }
            bufferString += "" + j + " not found, does not exist in the tree\n";
            return;
        }
        
        if( BST[i].val == j )
        {
            blip(i , Color.white);
            blip(i , Color.blue);
            bufferString += "" + j + " found\n";
        }
        else if( BST[i].val < j )
        {
            blip(i , Color.WHITE);
            BinarySearch(right(i) , j);
        }
        else if(BST[i].val > j)
        {
            blip(i , Color.white);
            BinarySearch(left(i) , j);
        }
        
        
    }
    
    
    public void blip(int i)
    {
        Graphics g1 = drawingBoard.getGraphics();
        Graphics2D g = (Graphics2D)g1;
        g.setStroke(new BasicStroke(2));
        g.setColor(Color.red);
        g.drawOval(BST[i].x - size , BST[i].y , 50 , 50);
        
        try
        {
            Thread.sleep(animationSpeed);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
        
        g.setColor(Color.black);
        
        g.drawOval(BST[i].x - size , BST[i].y , 50 , 50);
    }
    
    
    public void blip(int i , Color r)
    {
        Graphics g1 = drawingBoard.getGraphics();
        Graphics2D g = (Graphics2D)g1;
        g.setStroke(new BasicStroke(2));
        g.setColor(r);
        g.drawOval(BST[i].x - size , BST[i].y , 50 , 50);
        
        try
        {
            Thread.sleep(animationSpeed);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
        
        g.setColor(Color.black);
        
        g.drawOval(BST[i].x - size , BST[i].y , 50 , 50);
        
    }
    
    
    
    public void insertNode(int i , int j)
    {
        
        
        
        if(i == 1 && BST[i].val == 0)
        {
            nv++;
            BST[i].val=j;
            BST[i].x = frameWidth/2 + changingXOffset;
            BST[i].y = 0 + 100*changingDepth;
            return;
        }
        
        if( BST[i].val == 0 ) 
        {
            nv++;
            BST[i].val = j;
            BST[i].x = frameWidth/2 + changingXOffset;
            BST[i].y = 0 + 100*changingDepth;
            if( i != 1) lastInsertedPos = i;
            return;
        }
        
        if( BST[i].val < j )
        {
            blip(i);
            //move to right
            changingDepth++;
            changingXOffset = changingXOffset + frameWidth/(int)Math.pow(2, changingDepth + 1);
            
            insertNode(right(i) , j);
        }
        else if(BST[i].val > j)
        {
            blip(i);
            //move to left
            changingDepth++;
            changingXOffset = changingXOffset - frameWidth/(int)Math.pow(2, changingDepth + 1);
            insertNode(left(i) , j);
        }
        else if(BST[i].val == j)
        {
            blip(i);
            return;
        }
        
        
    
    }
    
    public void insertNode(int i , int j , boolean animate)
    {
        
        
        
        if(i == 1 && BST[i].val == 0)
        {
            nv++;
            BST[i].val=j;
            BST[i].x = frameWidth/2 + changingXOffset;
            BST[i].y = 0 + 100*changingDepth;
            return;
        }
        
        if( BST[i].val == 0 )
        {
            nv++;
            BST[i].val = j;
            BST[i].x = frameWidth/2 + changingXOffset;
            BST[i].y = 0 + 100*changingDepth;
            if( i != 1) lastInsertedPos = i;
            return;
        }
        
        if( BST[i].val < j )
        {
            if(animate) blip(i);
            //move to right
            changingDepth++;
            changingXOffset = changingXOffset + frameWidth/(int)Math.pow(2, changingDepth + 1);
            
            insertNode(right(i) , j , animate);
        }
        else if(BST[i].val > j)
        {
            if(animate) blip(i);
            //move to left
            changingDepth++;
            changingXOffset = changingXOffset - frameWidth/(int)Math.pow(2, changingDepth + 1);
            insertNode(left(i) , j , animate);
        }
        else if(BST[i].val == j)
        {
            if(animate) blip(i);
            return;
        }
        
        
    
    }
    
    
    
    
    
    public void drawNode(Graphics g1 , int x , int y, String nodeText){
        
        Graphics2D g = (Graphics2D)g1;
        g.setStroke(new BasicStroke(2));
        g.setColor(Color.GREEN); //make the node green
        g.fillOval(x - size, y, size, size);
        g.setColor(Color.BLACK);
        g.drawOval(x - size, y, size, size); //draws a black border on the node
        g.setColor(Color.RED);
        if(!hasBeenResized)
        {
            g.setFont(resize(g.getFont() , 2));
            hasBeenResized = true;
        }  
        g.drawString(nodeText, x - nodeTextOffset, y + size/2);
        if(lastInsertedPos != -1)
        {   
            g.setStroke(new BasicStroke(1));
            int parPos = parent(lastInsertedPos);
            g.setColor(Color.BLACK);
            g.drawLine(  BST[parPos].x - size/2  , BST[parPos].y + size , BST[lastInsertedPos].x - size/2  , BST[lastInsertedPos].y );   
            
            blip(lastInsertedPos , Color.YELLOW);
            
        }
        
        
    }
    
    public void drawNode(int i)
    {
        drawNode(drawingBoard.getGraphics() , BST[i].x , BST[i].y , Integer.toString(BST[i].val) );
    }
    
    public void connectNodes(int i , int j)
    {
        if( i == j ) return;
        
        Graphics2D g = (Graphics2D)drawingBoard.getGraphics();
        g.drawLine( BST[i].x - size/2 , BST[i].y + size , BST[j].x - size/2 , BST[j].y );
    }
    
    public void popLeaf(int i)
    {
        
        Graphics2D g = (Graphics2D)drawingBoard.getGraphics();
        g.setColor(g.getBackground());
        g.setStroke(new BasicStroke(2));
        g.fillOval(BST[i].x - size , BST[i].y , size , size);
        g.drawOval(BST[i].x - size , BST[i].y , size , size);
        g.drawLine( BST[parent(i)].x - size/2 , BST[parent(i)].y + size , BST[i].x - size/2 , BST[i].y );
        BST[i].val = 0;
    }
    
    
    
    public Font resize(Font f , float newSize)
    {
        return f.deriveFont(f.getSize() * newSize);
    }
    
}
