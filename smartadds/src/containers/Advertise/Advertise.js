import React from 'react'
import './Advertise.css'

function advertise({imgPath, contentText, contentType}) {
    let Content;
    console.log(imgPath)
    if ( contentType === 'image' ) {
        Content = <img alt="Advertisement Logo" className="Advertise-image" src={imgPath} />;
    } else {
        Content = <h1 className="Advertise-text">{contentText}</h1>;
    }
    return (
    <div className="Advertise-container">
        {Content}
    </div>
    );
}

export default advertise;