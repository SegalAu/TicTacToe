import py4j.GatewayServer;
import java.net.Inet4Address; 
import java.net.InetAddress;
import java.util.*; 
import java.util.stream.IntStream; 
import java.util.ArrayList;
import java.util.Arrays;




public class Game {

  public int[] runGame(int[] list) {
    
    System.out.println("Game: RUNNING GAME!");
    
   
    /*
      Check game results
      retursn
      0: ongoing
      1: tie
      2: Human wins
      3: Computer wins
    */
    int currResult = checkGameResult(list);

    // if tie, return all 3's
    if (currResult == 1) {
      int[] returnList = {3, 3, 3, 3, 3, 3, 3, 3, 3};
      return returnList;
    }

    // if loss, return all 4's
    if (currResult == 2) {
      int[] returnList = {4, 4, 4, 4, 4, 4, 4, 4, 4};
      return returnList;
    }

    if (currResult == 3) {
      int[] returnList = {5, 5, 5, 5, 5, 5, 5, 5, 5};
      return returnList;
    }

    // find all possible moves
    // find best move (playouts 2000)
    // return array

    int indexOfMove = determineMove(list);
    System.out.println("getting back index for move!");
    System.out.println(indexOfMove);
    
    int[] testArr = getAllMoves(list);
    System.out.println("getting back options for move!");
    for (int i = 0; i < testArr.length; i++ ) {
      System.out.print(testArr[i]);
    }

    int rnd = new Random().nextInt(testArr.length);
    int indexFinal = testArr[rnd];
    System.out.println("new index!");
    System.out.println(rnd);
    System.out.println("index to be moved to is:");
    System.out.println(indexFinal);

    // Perform move
    list[indexFinal] = 2;

    if(checkGameResult(list) == 3) {
      int[] returnList = {5, 5, 5, 5, 5, 5, 5, 5, 5};
      System.out.println("Computer Won!");
      return returnList;
    }

    System.out.println("Result after computer move");
    System.out.println(checkGameResult(list));

    System.out.println("sending game array now!");
    for (int i = 0; i < list.length; i++) {
      System.out.print(list[i]);
    }

    return list;
  }

  public int simulatePlayout(int[] temp_game_state, String turn) {
    String next = turn;
    int[] possible_moves = getAllMoves(temp_game_state);
    List<Integer> possible_moves_list = new ArrayList<Integer>(possible_moves.length);
    for (int i : possible_moves) {
      possible_moves_list.add(i);
    }
    int upperbound = possible_moves.length;    
    Random rand = new Random();
    while(checkGameResult(temp_game_state) == 0) {
      if (next == "Human") {
        upperbound--;
        if (upperbound < 0) {break;}
        int next_human_move_index = (upperbound == 0) ? 0 : rand.nextInt(upperbound);
        temp_game_state[next_human_move_index] = 1;
        possible_moves_list.remove(next_human_move_index);
        next = "Computer";        
      } else {
        upperbound--;
        if (upperbound < 0) {break;}
        int next_computer_move_index = (upperbound == 0) ? 0 : rand.nextInt(upperbound);
        temp_game_state[next_computer_move_index] = 2;
        possible_moves_list.remove(next_computer_move_index);
        next = "Human";        
      }
    }

    return checkGameResult(temp_game_state);
  }

  public int determineMove(int[] list) {
    // Create list of all legal moves
    int[] computer_moves = getAllMoves(list);

    // Create 2d array to store results
    int[] results_arr = new int[computer_moves.length];

    Arrays.fill(results_arr, 0);


    // List<Integer> results_arr = new ArrayList<Integer>(Collections.nCopies(computer_moves.length, 0));


    int result;
    int returnVal;

    // simulate playout of each move
    for (int i = 0; i < computer_moves.length; i++) {

      for (int j = 0; j < 1000; j++) {
        
        int[] temp_game_state = list.clone();
        temp_game_state[computer_moves[i]] = 2;

        result = simulatePlayout(temp_game_state, "Human");

        if (result == 1) {
          results_arr[i] += 1; // human wins
        } 
      }
      // Record number of wins / losses / ties for each possible move
      // ties[i] = results_arr[i][0];
      // loss.set(i,results_arr[i][1]);
      System.out.println("simulation!");
      System.out.println(results_arr[0]);
      System.out.println(results_arr[1]);
      // wins[i] = results_arr[i][2]; 
    }

    // Choose move with least amount of losses
    int cur_min = results_arr[0];
    int cur_min_index = 0;
    for (int i = 0; i < results_arr.length; i++ ) {
      if (results_arr[i] < cur_min) {
        cur_min = results_arr[i];
        cur_min_index = i;
      }
    }

    Random rand = new Random();
    int next_index = rand.nextInt(results_arr.length);
    System.out.println("random integer!");
    System.out.println(next_index);    

    return computer_moves[cur_min_index];
  }

  // return indexes of all possible moves
  public int[] getAllMoves(int[] list) {
    List<Integer> returnList = new ArrayList<Integer>();
    for(int i = 0; i < list.length; i++) {
      if (list[i] == 0) {
        returnList.add(i);
      }
    }

    int[] returnArr = new int[returnList.size()];
    for (int i = 0; i < returnList.size(); i++) {
      returnArr[i] = returnList.get(i);
    }
    return returnArr;
  }

  // Return:
  /*
    0 : ongoing,
    1 : tie
    2 : loss
    3 : win
  */ 
  public int checkGameResult(int[] list) {
    int diagonalsResult = checkDiagonals(list);
    int horizontalResult = checkHorizontal(list);
    int verticalResult = checkVertical(list);
    boolean tieResult = checkIsTie(list);
    if (tieResult) {
      return 1;
    }

    if(diagonalsResult != 0) { 
      if (diagonalsResult == 2) {
        return 2;
      }
      if (diagonalsResult == 3) {
        return 3;
      }
    } 

   if(horizontalResult != 0) { 
      if (horizontalResult == 2) {
        return 2;
      }
      if (horizontalResult == 3) {
        return 3;
      }
    } 
    
    if(verticalResult != 0) { 
      if (verticalResult == 2) {
        return 2;
      }
      if (verticalResult == 3) {
        return 3;
      }
    }

    return 0;
  }

  // Check if game is tie
  public boolean checkIsTie(int[] list) {
    for(int i = 0; i < list.length; i++) {
      if(list[i] == 0) {
        return false;
      }
    }

    return true;
  }

  /*
    Returns:
      0: Ongoing
      2: Human Wins
      3: Computer Wins
  */
  public int checkDiagonals(int[] list) {
    int[] lr_diagonal = {list[0], list[4], list[8]};
    boolean allComputer = false;
    boolean allHuman = false;
    boolean containsEmpty = IntStream.of(lr_diagonal).anyMatch(x -> x == 0);
    if (!containsEmpty) {
      allComputer = IntStream.of(lr_diagonal).allMatch(x -> x == 2);
      allHuman = IntStream.of(lr_diagonal).allMatch(x -> x == 1);
      if (allComputer == true) {
        return 3;
      } 
      if (allHuman == true) {
        return 2;
      }
    }

    int[] rl_diagonal = {list[2], list[4], list[6]};
    containsEmpty = IntStream.of(rl_diagonal).anyMatch(x -> x == 0);
    if (!containsEmpty) {
      allComputer = IntStream.of(rl_diagonal).allMatch(x -> x == 2);
      allHuman = IntStream.of(rl_diagonal).allMatch(x -> x == 1);
      if (allComputer == true) {
        return 3;
      } 
      if (allHuman == true) {
        return 2;
      }
    }

    return 0;    
  }

  /*
    Returns:
      0: Ongoing
      2: Human Wins
      3: Computer Wins
  */
  public int checkVertical(int[] list) {
    boolean allHuman = false;
    boolean allComputer = false;
    for (int i = 0; i < 3; i++) {
      int[] vertical_arr = {list[i], list[i+3], list[i+6]};
      boolean containsEmpty = IntStream.of(vertical_arr).anyMatch(x -> x == 0);
      if (!containsEmpty) {
        allComputer = IntStream.of(vertical_arr).allMatch(x -> x == 2);
        allHuman = IntStream.of(vertical_arr).allMatch(x -> x == 1);
        if (allComputer == true) {
          return 3;
        }
        if (allHuman == true) {
          return 2;
        }
      }
    }

    return 0;
  }

  /*
    Returns:
      0: Ongoing
      2: Human Wins
      3: Computer Wins
  */
  public int checkHorizontal(int[] list) {
    for (int i = 0; i < list.length; i++) {

      if (i % 3 == 0) {
        int[] horizontal_arr = {list[i], list[i+1], list[i+2]};
        boolean containsEmpty = IntStream.of(horizontal_arr).anyMatch(x -> x == 0);
        if (!containsEmpty) {
          boolean allComputer = IntStream.of(horizontal_arr).allMatch(x -> x == 2);
          boolean allHuman = IntStream.of(horizontal_arr).allMatch(x -> x == 1);
          if (allComputer == true) {
            return 3;
          }
          if (allHuman == true) {
            return 2;
          }
        }
      }
      
    }

    return 0;
  }

  public static void main(String[] args) {
    Game app = new Game();

    
    // System.setProperty("java.net.preferIPv4Stack", "true");
    // InetAddress addr = Inet4Address.getByName("0.0.0.0");

    // // app is now the gateway.entry_point
    // GatewayServer.GatewayServerBuilder builder = new GatewayServer.GatewayServerBuilder(app);
    // builder.javaAddress(addr);
    GatewayServer server = new GatewayServer(app);
    server.start();
    System.out.println("Game Started!");
  }
}
