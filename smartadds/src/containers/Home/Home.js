import React from 'react'
import QRCode from 'qrcode.react'
import './Home.css'

function home(props) {
    return (
    <div className="Qrcode-center">
        <h2 style={{color:"black", marginBottom:"60px"}}>Scan the QR code </h2>
        <div style={{margin: "0 auto", width:"140px"}}>
            <QRCode value="qw12badhs" />
        </div>
    </div>
    );
}

export default home;