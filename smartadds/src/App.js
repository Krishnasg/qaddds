import React, { Component } from 'react';
import './App.css';
import HeaderNavBar from './components/HeaderNavBar/HeaderNavBar'
import Home from './containers/Home/Home'
import Advertise from './containers/Advertise/Advertise'

class App extends Component {
  state = {
    showAdvertise: false,
    advertiseContent: {
      contentType: '',
      contentText: '',
      imgPath: ''
    }
  };

  getPostContent = async () => {
      let response = await fetch('http://192.168.1.9:3008/post/get')
      let JsonData = await response.json()
      let values = this.state
      console.log(JsonData)
      values.showAdvertise = JsonData.advertiseContent.contentType ? true : false
      values.advertiseContent = JsonData.advertiseContent
      this.setState({values})
  }

  componentDidMount() {
    // repeat with the interval of 1 seconds
    setInterval(() => this.getPostContent(), 1000);
  }

  render() {
    let {showAdvertise} = this.state;
    return (
      <div className="App">
      {showAdvertise === false ? <>
        <HeaderNavBar />
        <Home />
        </>
        : <Advertise {...this.state.advertiseContent}/>}
      </div>
    );
  }
}

export default App;
