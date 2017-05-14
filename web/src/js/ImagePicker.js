/**
 * Created by Sasha on 5/14/17.
 */

import React from 'react'


export default class ImagePicker extends React.Component{

    constructor(props) {
        super(props);
        this.state = {}
    }

    selectItem(){
        this.fileinput.click();
    }

    onItemChanged(){
        let reader  = new FileReader();
        let file = this.fileinput.files[0];
        this.props.onChanged(file);
        reader.readAsDataURL(file);
        reader.onload = (event) => {
            this.setState({picture: reader.result});
        }
    }



    render(){
        return <div>
            {this.state.picture && <img className="img-responsive col-sm-offset-1" src={this.state.picture}/>}
            <div className="btn btn-info" onClick={() => this.selectItem()}>Select image</div>
            <input ref={input => this.fileinput = input} onChange={() => this.onItemChanged()} className="hidden" type="file"/>
        </div>

    }

}