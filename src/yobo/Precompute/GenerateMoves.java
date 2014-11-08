package yobo.Precompute;

/**
 * Created by IntelliJ IDEA.
 * User: Christophe Job
 * Date: Feb 21, 2004
 * Time: 4:22:22 PM
 * To change this template use File | Settings | File Templates.
 */


public class GenerateMoves {

    static int testForMove(int initialPosition, int direction) {
        switch(direction) {
            case 0: return (initialPosition < 56 ) ? initialPosition + 8 : -1;
            case 1: return (initialPosition < 56 ) && (initialPosition & 7) < 7 ? initialPosition + 9 : -1;
            case 2: return (initialPosition & 7) < 7 ? initialPosition + 1 : -1;
            case 3: return (initialPosition > 7 ) && (initialPosition & 7) < 7 ? initialPosition - 7 : -1;
            case 4: return (initialPosition > 7 ) ? initialPosition - 8 : -1;
            case 5: return (initialPosition > 7 ) && (initialPosition & 7) > 0  ? initialPosition -9 : -1;
            case 6: return (initialPosition & 7) > 0 ? initialPosition - 1 : -1;
            case 7: return (initialPosition < 56 ) && (initialPosition & 7) > 0  ? initialPosition + 7 : -1;
        }
        // should never get there
        return 0;
    };

    static int knightTestforMove(int initialPosition, int direction) {
        switch(direction) {
            case 0: return (initialPosition < 48 ) && (initialPosition & 7) < 7 ? initialPosition + 17 : -1;
            case 1: return (initialPosition < 56 ) && (initialPosition & 7) < 6 ? initialPosition + 10 : -1;
            case 2: return (initialPosition > 7) &&  (initialPosition & 7) < 6  ? initialPosition - 6 : -1;
            case 3: return (initialPosition > 15 ) && (initialPosition & 7) < 7 ? initialPosition - 15 : -1;
            case 4: return (initialPosition > 15 ) && (initialPosition & 7) > 0 ? initialPosition - 17: -1;
            case 5: return (initialPosition > 7 ) && (initialPosition & 7) > 1  ? initialPosition - 10 : -1;
            case 6: return (initialPosition < 56 ) && (initialPosition & 7) > 1  ? initialPosition + 6 : -1;
            case 7: return (initialPosition < 48 ) && (initialPosition & 7) > 0  ? initialPosition + 15 : -1;
        }
        // should never get there
        return 0;
    };
    // direction are 0,1,2,3 are the "take" moves
    // direction 4 and 5 are pushing one or pushing two
    static int pawnTestforMove(int initialPosition, int direction, boolean computer) {
        if (computer)
        {
            switch(direction) {
                case 0: return (initialPosition < 56 ) && (initialPosition & 7) > 0 ? initialPosition + 7 : -1;
//                case 1: return (initialPosition < 16 ) && (initialPosition > 7 ) && (initialPosition & 7) > 0 ? initialPosition + 15 : -1;
                case 1: return (initialPosition < 56) &&  (initialPosition & 7) < 7  ? initialPosition + 9 : -1;
//                case 3: return  (initialPosition < 16 ) && (initialPosition > 7 ) &&  (initialPosition & 7) < 7  ? initialPosition + 17 : -1;
                case 2: return (initialPosition < 56 ) ? initialPosition + 8 : -1;
                case 3: return (initialPosition < 16 ) && (initialPosition > 7 ) ? initialPosition + 16 : -1;
            }
        } else
        {
            switch(direction) {
                case 0: return (initialPosition > 7 ) && (initialPosition & 7) > 0 ? initialPosition - 9 : -1;
//                case 1: return (initialPosition > 47 ) && (initialPosition < 56) && (initialPosition & 7) > 0 ? initialPosition - 17 : -1;
                case 1: return (initialPosition > 7) &&  (initialPosition & 7) < 7  ? initialPosition - 7 : -1;
//                case 3: return  (initialPosition > 47 ) && (initialPosition < 56) && (initialPosition & 7) < 7  ? initialPosition - 15 : -1;
                case 2: return (initialPosition > 7 ) ? initialPosition - 8 : -1;
                case 3: return (initialPosition > 47 ) && (initialPosition < 56) ? initialPosition - 16 : -1;
            }
        }
        // should never get there
        return 0;
    };

    public static void main(String arg[]) {
        /* gemerate Moves for Tower */
        System.out.println("private static byte Towermoves[][][] = {");
        for(int BoardPosition=0; BoardPosition < 64; BoardPosition++)
        {
            // Loop on directions 0 = noon, going clockwise
            int initialPosition;

            System.out.print("/* Position " + BoardPosition+ " */");
            if (BoardPosition != 0)
                System.out.print(",{");
            else
                System.out.print("{");


            for (int direction = 0; direction < 8 ; direction +=2 ) {
                initialPosition = BoardPosition;
                if (direction!=0)
                    System.out.print( ",{");
                else
                    System.out.print( "{");
                while (testForMove(initialPosition,direction) != -1 ) {
                    // not very efficient (we call it twice)
                    if (initialPosition != BoardPosition)
                        System.out.print(",");
                    initialPosition = testForMove(initialPosition,direction);
                    System.out.print(initialPosition);
                }
                System.out.print("}");
            }
            System.out.println("}");
        }
        System.out.println("};");


        /* gemerate Moves for computer Pawn */
        System.out.println("private static byte ComputerPawnmoves[][][] = {");
        for(int BoardPosition=0; BoardPosition < 64; BoardPosition++)
        {
            // Loop on directions 0 = noon, going clockwise
            int initialPosition;

            System.out.print("/* Position " + BoardPosition+ " */");
            if (BoardPosition != 0)
                System.out.print(",{");
            else
                System.out.print("{");


            for (int direction = 0; direction < 4 ; direction ++ ) {
                initialPosition = BoardPosition;
                if (direction!=0)
                    System.out.print( ",{");
                else
                    System.out.print( "{");
                if (pawnTestforMove(initialPosition,direction,true) != -1 ) {
                    // not very efficient (we call it twice)
                    if (initialPosition != BoardPosition)
                        System.out.print(",");
                    initialPosition = pawnTestforMove(initialPosition,direction,true);
                    System.out.print(initialPosition);
                }
                System.out.print("}");
            }
            System.out.println("}");
        }
        System.out.println("};");

        /* gemerate Moves for Opponent Pawn */
        System.out.println("private static byte OpponentPawnmoves[][][] = {");
        for(int BoardPosition=0; BoardPosition < 64; BoardPosition++)
        {
            // Loop on directions 0 = noon, going clockwise
            int initialPosition;

            System.out.print("/* Position " + BoardPosition+ " */");
            if (BoardPosition != 0)
                System.out.print(",{");
            else
                System.out.print("{");


            for (int direction = 0; direction < 4 ; direction ++ ) {
                initialPosition = BoardPosition;
                if (direction!=0)
                    System.out.print( ",{");
                else
                    System.out.print( "{");
                if (pawnTestforMove(initialPosition,direction,false) != -1 ) {
                    // not very efficient (we call it twice)
                    if (initialPosition != BoardPosition)
                        System.out.print(",");
                    initialPosition = pawnTestforMove(initialPosition,direction,false);
                    System.out.print(initialPosition);
                }
                System.out.print("}");
            }
            System.out.println("}");
        }
        System.out.println("};");


        /* gemerate Moves for Bishop */
        System.out.println("private static byte Bishopmoves[][][] = {");
        for(int BoardPosition=0; BoardPosition < 64; BoardPosition++)
        {
            // Loop on directions 0 = noon, going clockwise
            int initialPosition;

            System.out.print("/* Position " + BoardPosition+ " */");
            if (BoardPosition != 0)
                System.out.print(",{");
            else
                System.out.print("{");


            for (int direction = 1; direction < 8 ; direction +=2 ) {
                initialPosition = BoardPosition;
                if (direction!= 1)
                    System.out.print( ",{");
                else
                    System.out.print( "{");
                while (testForMove(initialPosition,direction) != -1 ) {
                    // not very efficient (we call it twice)
                    if (initialPosition != BoardPosition)
                        System.out.print(",");
                    initialPosition = testForMove(initialPosition,direction);
                    System.out.print(initialPosition);
                }
                System.out.print("}");
            }
            System.out.println("}");
        }
        System.out.println("};");

        /* gemerate Moves for knight */
        System.out.println("private static byte Knightmoves[][][] = {");
        for(int BoardPosition=0; BoardPosition < 64; BoardPosition++)
        {
            // Loop on directions 0 = noon, going clockwise
            int initialPosition;

            System.out.print("/* Position " + BoardPosition+ " */");
            if (BoardPosition != 0)
                System.out.print(",{");
            else
                System.out.print("{");


            for (int direction = 0; direction < 8 ; direction++ ) {
                initialPosition = BoardPosition;
                if (direction!=0)
                    System.out.print( ",{");
                else
                    System.out.print( "{");
                if (knightTestforMove(initialPosition,direction) != -1 ) {
                    // not very efficient (we call it twice)
                    if (initialPosition != BoardPosition)
                        System.out.print(",");
                    System.out.print(knightTestforMove(initialPosition,direction));
                }
                System.out.print("}");
            }
            System.out.println("}");
        }
        System.out.println("};");

    /* gemerate Moves for queen */
    System.out.println("private static byte Queenmoves[][][] = {");
    for(int BoardPosition=0; BoardPosition < 64; BoardPosition++)
    {
        // Loop on directions 0 = noon, going clockwise
        int initialPosition;

        System.out.print("/* Position " + BoardPosition+ " */");
        if (BoardPosition != 0)
            System.out.print(",{");
        else
            System.out.print("{");


        for (int direction = 0; direction < 8 ; direction++ ) {
            initialPosition = BoardPosition;
            if (direction!=0)
                System.out.print( ",{");
            else
                System.out.print( "{");
            while (testForMove(initialPosition,direction) != -1 ) {
                // not very efficient (we call it twice)
                if (initialPosition != BoardPosition)
                    System.out.print(",");
                initialPosition = testForMove(initialPosition,direction);
                System.out.print(initialPosition);
            }
            System.out.print("}");
        }
        System.out.println("}");
    }
    System.out.println("};");

        /* gemerate Moves for King */
        System.out.println("private static byte Kingmoves[][][] = {");
        for(int BoardPosition=0; BoardPosition < 64; BoardPosition++)
        {
            // Loop on directions 0 = noon, going clockwise
            int initialPosition;

            System.out.print("/* Position " + BoardPosition+ " */");
            if (BoardPosition != 0)
                System.out.print(",{");
            else
                System.out.print("{");


            for (int direction = 0; direction < 8 ; direction++ ) {
                initialPosition = BoardPosition;
                if (direction!=0)
                    System.out.print( ",{");
                else
                    System.out.print( "{");
                if (testForMove(initialPosition,direction) != -1 ) {
                    // not very efficient (we call it twice)
                    if (initialPosition != BoardPosition)
                        System.out.print(",");
                    System.out.print(testForMove(initialPosition,direction));
                }
                System.out.print("}");
            }
            System.out.println("}");
        }
        System.out.println("};");

    }
}
