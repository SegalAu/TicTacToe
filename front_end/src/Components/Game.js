import React from 'react';
import { withStyles } from '@material-ui/core/styles';
import socketIOClient from "socket.io-client";
import Button from "@material-ui/core/Button";
import Modal from '@material-ui/core/Modal';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';
const ENDPOINT ="http://127.0.0.1:4001"; 


const styles = theme => ({
});





// const socket = socketIOClient(ENDPOINT, {transports: ['websocket']});

class Game extends React.Component {
    constructor(props) {
      super(props);
      this.state = {
        gameArray: [0, 0, 0, 0, 0, 0, 0, 0, 0],
        ul_disabled: false,        
        um_disabled: false,
        ur_disabled: false,
        ml_disabled: false,
        mm_disabled: false,
        mr_disabled: false,
        ll_disabled: false,
        lm_disabled: false,
        lr_disabled: false,
        ul_state: "_",
        um_state: "_",
        ur_state: "_",
        ml_state: "_",
        mm_state: "_",
        mr_state: "_",
        ll_state: "_",
        lm_state: "_",
        lr_state: "_",
        modal : false,
        reset : false
      }

      this.handleClick = this.handleClick.bind(this);
    }

    componentDidMount() {
      let socket = socketIOClient(ENDPOINT, {transports: ['websocket']});
      socket.on("move_update", (data) => {
        console.log(data);
        // tie
        if(data[0] === 3) {
          let resetVec = [0, 0, 0, 0, 0, 0, 0, 0, 0];
          this.setState({
            gameArray : resetVec,
            endMessage: "Tie! Play again!",
            reset : true
          });
          this.handleOpen();
        }
        // if human won
        if(data[0] === 4) {
          let resetVec = [0, 0, 0, 0, 0, 0, 0, 0, 0];
          this.setState({
            gameArray : resetVec,
            endMessage: "You won! Congratulations",
            reset : true
          });
          this.forceUpdate();
          this.handleOpen();
        }

        if (data[0] === 5) {
          let resetVec = [0, 0, 0, 0, 0, 0, 0, 0, 0];
          this.setState({
            gameArray : resetVec,
            endMessage: "You lost! Better luck next time!",
            reset : true
          });
          this.forceUpdate();
          this.handleOpen();
        }

        this.setState({
          gameArray: data          
        });
        
        
      })
    }

    handleClick(pos) {
        switch (pos) {
          case "ul":
            if (this.state.gameArray[0] === 0) {
              let returnArr = this.state.gameArray;
              returnArr[0] = 1;
              this.setState({
                ul_state : "O",
                gameArray : returnArr
              });
            }
            break;
          case "um":
            if (this.state.gameArray[1] === 0) {
              let returnArr = this.state.gameArray;
              returnArr[1] = 1;
              this.setState({
                um_state : "O",
                gameArray : returnArr
              });
            }
            break;
          case "ur":
            if (this.state.gameArray[2] === 0) {
              let returnArr = this.state.gameArray;
              returnArr[2] = 1;
              this.setState({
                ur_state : "O",
                gameArray : returnArr
              });
            }
            break;
          case "ml":
            if (this.state.gameArray[3] === 0) {
              let returnArr = this.state.gameArray;
              returnArr[3] = 1;
              this.setState({
                ml_state : "O",
                gameArray : returnArr
              });
            }
            break;
          case "mm":
            if (this.state.gameArray[4] === 0) {
              let returnArr = this.state.gameArray;
              returnArr[4] = 1;
              this.setState({
                mm_state : "O",
                gameArray : returnArr
              });
            }
            break;
          case "mr":
            if (this.state.gameArray[5] === 0) {
              let returnArr = this.state.gameArray;
              returnArr[5] = 1;
              this.setState({
                mr_state : "O",
                mr_disabled : true,
                gameArray : returnArr
              });
            }
            break;
          case "ll":
            if (this.state.gameArray[6] === 0) {
              let returnArr = this.state.gameArray;
              returnArr[6] = 1;
              this.setState({
                ll_state : "O",
                ll_disabled : true,
                gameArray : returnArr
              });
            }
            break;
          case "lm":
            if (this.state.gameArray[7] === 0) {
              let returnArr = this.state.gameArray;
              returnArr[7] = 1;
              this.setState({
                lm_state : "O",
                lm_disabled : true,
                gameArray : returnArr
              });
            }
            break;
          case "lr":
            if (this.state.gameArray[8] === 0) {
              let returnArr = this.state.gameArray;
              returnArr[8] = 1;
              this.setState({
                lr_state : "O",
                lr_disabled : true,
                gameArray : returnArr
              });
            }
            break;
          default:
            console.log("hit default");
            break;
        }
        let socket = socketIOClient(ENDPOINT, {transports: ['websocket']});
        socket.connect();
        console.log("state after human move!");
        console.log(this.state.gameArray);
        socket.emit("move", this.state.gameArray);        
    }

    
    handleOpen = () => {
      this.setState({
          modal: true
      });
    };
  
    handleClose = () => {
      this.setState({
          modal: false
      });
      if (this.state.reset === true) {
        this.setState({
          reset : false,
          gameArray: [0, 0, 0, 0, 0, 0, 0, 0, 0],
          ul_state: "_",
          um_state: "_",
          ur_state: "_",
          ml_state: "_",
          mm_state: "_",
          mr_state: "_",
          ll_state: "_",
          lm_state: "_",
          lr_state: "_",
        });
      }
    };

    render() {
      let stylesRender = {
        outerContainer: {
          width: "%100",
          justifyContent: "center",
          alignItems: "center",
        },

        container: {
          width: "200px",
          height: "900px",
          paddingLeft: "800px",
          paddingTop: "300px"
        },

        buttonStyle: {
          height: "1000px"
        }
      }

      

      return (
        <div style = {stylesRender.outerContainer}>

            <Card>
              <CardContent>
                <Typography gutterBottom variant="h5" component="h2">
                  Tic Tac Toe
                </Typography>
                <Typography variant="body2" color="textSecondary" component="p">
                  Press a square to start! Good luck!
                </Typography>
              </CardContent>
              </Card>
          <div style = {stylesRender.container}>

            
          
            <Button variant="outlined" 
            onClick={() => this.handleClick("ul")} >{(this.state.gameArray[0] === 2) ? "X" : this.state.ul_state }</Button>
    
            <Button variant="outlined" styles={stylesRender.buttonStyle} 
            onClick={() => this.handleClick("um")} >{(this.state.gameArray[1] === 2) ? "X" : this.state.um_state}</Button>
    
            <Button variant="outlined" styles={stylesRender.buttonStyle}  
            onClick={() => this.handleClick("ur")} >{(this.state.gameArray[2] === 2) ? "X" : this.state.ur_state}</Button>
      
            <Button variant="outlined" styles={stylesRender.buttonStyle}
            onClick={() => this.handleClick("ml")}>{(this.state.gameArray[3] === 2) ? "X" : this.state.ml_state}</Button>
        
            <Button variant="outlined" styles={stylesRender.buttonStyle}
            onClick={() => this.handleClick("mm")}>{(this.state.gameArray[4] === 2) ? "X" : this.state.mm_state}</Button>
    
            <Button variant="outlined" styles={stylesRender.buttonStyle}
            onClick={() => this.handleClick("mr")}>{(this.state.gameArray[5] === 2) ? "X" : this.state.mr_state}</Button>

            <Button variant="outlined" styles={stylesRender.buttonStyle}
            onClick={() => this.handleClick("ll")}>{(this.state.gameArray[6] === 2) ? "X" : this.state.ll_state}</Button>
        
            <Button variant="outlined" styles={stylesRender.buttonStyle}
            onClick={() => this.handleClick("lm")}>{(this.state.gameArray[7] === 2) ? "X" : this.state.lm_state}</Button>
    
            <Button variant="outlined" styles={stylesRender.buttonStyle}
            onClick={() => this.handleClick("lr")}>{(this.state.gameArray[8] === 2) ? "X" : this.state.lr_state}</Button>

            <div>
              {/* <button type="button" onClick={this.handleOpen}>
                {this.state.endMessage}
              </button> */}
              <Modal
                open={this.state.modal}
                onClose={this.handleClose}
                aria-labelledby="simple-modal-title"
                aria-describedby="simple-modal-description"
              >
                <div >
                  <h2 id="simple-modal-title">Game Over</h2>
                  <p id="simple-modal-description">
                  {this.state.endMessage}
                  </p>
                </div>
              </Modal>
            </div>

          </div>
        </div>
        
      );
    }
    

    
}

export default withStyles(styles)(Game);