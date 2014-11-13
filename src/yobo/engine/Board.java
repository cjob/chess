package yobo.engine;

import java.applet.Applet;


/**
 * Created by IntelliJ IDEA.
 * User: Christophe Job
 * Date: Feb 21, 2004
 * Time: 4:13:56 PM
 * To change this template use File | Settings | File Templates.
 */
public final class Board implements Runnable {
    // ugly it breaks the encapsulation
    static public Applet applet;
    static public Piece BoardArray[];
    static int maxDepth = 100;
    static int minDepth = 6;
    static int maxNodeCount;
    static int maxDepthReached;
    static byte moveArray[][] = new byte[maxDepth+1][600];
    static byte checkmoveArray[] = new byte[600];
    static int BestMoveStart[] = new int[maxDepth+1];
    static int BestMoveEnd[] = new int[maxDepth+1];
    static int numLeaf;
    static long elapsedTime;
    static int currentPositionScore;

    public Board(Applet a) {
        BoardArray = new Piece[64];
        initBoard();
        applet = a;
    };




    // Evaluate a leaf node by summing up the value of the different pieces
    // subtracting the value of the opponent pieces
    int EvaluateLeaf()
    {
        int tempTotal = 0;
        Piece p;
        for (int i =0 ; i < 64 ; i++) {
            p = BoardArray[i];
            if (p != null)
            {
                // value more the pawns that are further down the board
                tempTotal += p.getValue();
                if (p == Piece.CPawn )
                    tempTotal += i ;
                else
                if (p == Piece.OPawn )
                    tempTotal -= 64 - i;
            }

        }
        numLeaf++;
        return tempTotal;
    };

    private int max(int a, int b)
    {
        return a > b ? a : b;
    }

    private int min(int a, int b)
    {
        return a < b ? a : b;
    }


    int Evaluate( int depth , int A, int B , int currentNodeCount)
    {
           Piece lastTarget,lastSource;
           int moveCount,checkMoveCount;
           int alpha,beta,val,src,dest;
           boolean check;
           int finalNodeCount;


           boolean computer = (depth & 1) == 0;
           // if we have readched the maximum depth of the tree
           if ((depth == maxDepth) ||
              // or of we are past the minimum and there is not enough nodes left to go another round
              // be careful that we always need to evaluate at the same depth
              (depth >= minDepth) && (currentNodeCount < 2500) && ((depth & 1) == (minDepth & 1)))
           {
                maxDepthReached = max(maxDepthReached,depth);
                return EvaluateLeaf();
           }
           else
           {

               BestMoveStart[depth]=-1;
               BestMoveEnd[depth]=-1;

               alpha = Integer.MIN_VALUE;
               beta = Integer.MAX_VALUE;

               finalNodeCount = numLeaf + currentNodeCount;

               moveCount = Piece.getMoveArray(moveArray[depth], BoardArray, computer );


               for (int i =0 ; i < moveCount ; i+=2)
               {

                   dest= moveArray[depth][i+1];
                   src=moveArray[depth][i];
                   // save the the piece at the target to be able to undo the move
                   lastTarget = BoardArray[dest];
                   lastSource = BoardArray[src];

                   // did we take a king ?
                   // if the opponent plays a move that makes him lose his King abort
                   if (lastTarget== Piece.OKing)
                        return Integer.MAX_VALUE-depth;
                   if (lastTarget== Piece.CKing)
                       return Integer.MIN_VALUE+depth;

                   // make the move
                   move(src,dest);


                   // if it is a MAX node
                   if (computer) {
                       val = Evaluate(depth+1,max(A,alpha),B,max(currentNodeCount/moveCount,(finalNodeCount - numLeaf)/(moveCount-i)));


                       // if the opponent is going to loose his King at the next move, make sure that he is in check
                       // otherwise it is a draw
                       if (val == Integer.MAX_VALUE - depth - 2)
                       {
                           // get all the moves
                           checkMoveCount = Piece.getMoveArray(checkmoveArray, BoardArray, computer );
                           // can the computer take it already
                           check= false;
                           for (int j=1; j < checkMoveCount ; j+=2)
                           {
                               if (BoardArray[checkmoveArray[j]] == Piece.OKing)
                               {
                                   check = true;
                                   break;
                               }
                           }
                           // if the opponent was not in check already, it is a pat
                           if (!check)
                               val =Integer.MAX_VALUE/2;
                       }

                       // undo the move
                       BoardArray[src] = lastSource;
                       BoardArray[dest]= lastTarget;


                       // if it found a batter move
                       if (val > alpha) {
                           alpha = val;
                           BestMoveStart[depth] = src;
                           BestMoveEnd[depth] = dest;
                       }
                       // if it is too good, return. We know the opponent wont take us down that path
                       if (alpha >= B)
                            return alpha;
                   } else {
                       // playing the opponent
                       val = Evaluate(depth+1,A,min(B,beta),max(currentNodeCount/moveCount,(finalNodeCount - numLeaf)/(moveCount-i)));

                       // if the computer is going to loose his King, make sure that he is in check
                       if (val == Integer.MIN_VALUE + depth + 2 )
                       {
                           // get all the moves
                           checkMoveCount = Piece.getMoveArray(checkmoveArray, BoardArray, computer );
                           // can the computer take it already
                           check= false;
                           for (int j=1; j < checkMoveCount ; j+=2)
                               if (BoardArray[checkmoveArray[j]] == Piece.CKing)
                               {
                                   check = true;
                                   break;
                               }
                           // if the computer was not in check already, it is a pat
                           if (!check)
                               val = Integer.MIN_VALUE/2;
                       }

                       // undo the move
                       BoardArray[src] = lastSource;
                       BoardArray[dest]= lastTarget;

                       if (val < beta) {
                           beta = val;
                           BestMoveStart[depth] = src;
                           BestMoveEnd[depth] = dest;
                       }
                       // if it is too bad, return. We know the opponent wont take us down that path
                       if (A >= beta )
                            return beta;
                   }

               }
           }
        if (computer)
             return alpha;
        else
             return beta;
    }





    public void initBoard() {

        BoardArray[0] = Piece.CTower;
        BoardArray[1] = Piece.CKnight;
        BoardArray[2] = Piece.CBishop;
        BoardArray[4] = Piece.CKing;
        BoardArray[3] = Piece.CQueen;
        BoardArray[5] = Piece.CBishop;
        BoardArray[6] = Piece.CKnight;
        BoardArray[7] = Piece.CTower;
        BoardArray[8] = Piece.CPawn;
        BoardArray[9] = Piece.CPawn;
        BoardArray[10] = Piece.CPawn;
        BoardArray[11] = Piece.CPawn;
        BoardArray[12] = Piece.CPawn;
        BoardArray[13] = Piece.CPawn;
        BoardArray[14] = Piece.CPawn;
        BoardArray[15] = Piece.CPawn;


        BoardArray[56] = Piece.OTower;
        BoardArray[57] = Piece.OKnight;
        BoardArray[58] = Piece.OBishop;
        BoardArray[60] = Piece.OKing;
        BoardArray[59] = Piece.OQueen;
        BoardArray[61] = Piece.OBishop;
        BoardArray[62] = Piece.OKnight;
        BoardArray[63] = Piece.OTower;
        BoardArray[48] = Piece.OPawn;
        BoardArray[49] = Piece.OPawn;
        BoardArray[50] = Piece.OPawn;
        BoardArray[51] = Piece.OPawn;
        BoardArray[52] = Piece.OPawn;
        BoardArray[53] = Piece.OPawn;
        BoardArray[54] = Piece.OPawn;
        BoardArray[55] = Piece.OPawn;

    };


    public void run() {
        numLeaf = 0;
        elapsedTime = System.currentTimeMillis();
        System.out.println("Thinking ...");
        // do not evaluate more than 10000000 nodes
        maxNodeCount = 15000000;
        currentPositionScore=Evaluate(0,Integer.MIN_VALUE,Integer.MAX_VALUE,maxNodeCount);
        System.out.println("Move From "+ BestMoveStart[0] +" to " + BestMoveEnd[0]);
        move(BestMoveStart[0], BestMoveEnd[0]);
        elapsedTime = System.currentTimeMillis() - elapsedTime;
        System.out.println("Looked at : " + numLeaf + " nodes in "+ elapsedTime + " ms => Average node/seconds: " + numLeaf/((max((int)elapsedTime,1000)/1000)));
        System.out.println("Maximum depth reached : " + maxDepthReached);
        System.out.println("Current Score : " + currentPositionScore);
        applet.repaint();

    }

    public void move(int src, int dest) {

        BoardArray[dest] = BoardArray[src];
        BoardArray[src] = null;

        // Promote the pawn to a queen (BUG!: it could be a knoght as well)
        if ((BoardArray[dest] == Piece.CPawn) && (dest > 55))
        {
            BoardArray[dest] = Piece.CQueen;
        } else
        // Promote the pawn to a queen (BUG!: it could be a knoght as well)
        if ((BoardArray[dest] == Piece.OPawn) && (dest <8))
        {
            BoardArray[dest] = Piece.OQueen;
        }
    }

}
