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

    int indexOfMove = determineMove(list);
    System.out.println("getting back index for move!");
    System.out.println(indexOfMove);
    
    int[] testArr = getAllMoves(list);
    System.out.println("getting back options for move!");
    for (int i = 0; i < testArr.length; i++ ) {
      System.out.print(testArr[i]);
    }

    // // Perform move
    list[indexOfMove] = 2;

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

  public int determineMove(int[] game_arr) {
    // Create list of all legal moves
    int[] computer_moves = getAllMoves(game_arr);
    int moves_size = computer_moves.length;
    // Create 2d array to store results
    int[] wins = new int[moves_size];
    int[] loss = new int[moves_size];
    int[] ties = new int[moves_size];
    Arrays.fill(wins, 0);
    Arrays.fill(loss, 0);
    Arrays.fill(ties, 0);
    int curr_wins = 0;
    int curr_loss = 0;
    int curr_ties = 0;
    int result;
    int returnVal;

    // simulate playout of each move
    for (int i = 0; i < computer_moves.length; ++i) {
      curr_wins = 0;
      curr_loss = 0;
      curr_ties = 0;
      for (int j = 0; j < 1000; ++j) {        
        int[] temp_game_state = game_arr.clone();
        temp_game_state[computer_moves[i]] = 2;
        result = simulatePlayout(temp_game_state, "Human");

        if (result == 1) {
          curr_ties += 1; 
        } else if (result == 2) {
          curr_loss += 1;
        } else {
          curr_wins += 1;
        }
      }

      wins[i] = curr_wins;
      loss[i] = curr_loss;
      ties[i] = curr_ties;
      
    }

    int min_loss_ind = 0;
    int min_loss = 1000;
    for (int i = 0; i < loss.length; ++i) {
      if (loss[i] < min_loss) {
        min_loss = loss[i];
        min_loss_ind = i;
      }
    }

    System.out.println("Index with least losses");
    System.out.println("index: " + min_loss_ind);

    return computer_moves[min_loss_ind];
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
