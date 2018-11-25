import React from 'react'
import Navbar from 'react-bootstrap/lib/Navbar'
import logo from '../../logo.jpeg'
import './HeaderNavBar.css';

function HeaderNavBar() {
    return (
        <Navbar style={{marginBottom: "0", backgroundColor: "#e1e5ea"}}>
        <Navbar.Header>
          <Navbar.Brand>
            <img alt="Logo of the company" src={logo} />
          </Navbar.Brand>
        </Navbar.Header>
        <Navbar.Collapse>
            <Navbar.Text>
                Easy Advertising
            </Navbar.Text>
            <Navbar.Text pullRight>Have a great day!</Navbar.Text>
        </Navbar.Collapse>
      </Navbar>
    );
}

export default HeaderNavBar;