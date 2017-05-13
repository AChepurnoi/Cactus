/**
 * Created by Sasha on 5/14/17.
 */


import React from 'react'
import ImagePicker from "./ImagePicker";
import * as axios from "axios";


export default class FormComponent extends React.Component {

    constructor(props) {
        super(props);
        this.state = {};
        this.client = axios.create({
            baseURL: "http://localhost:3000/api/",
            responseType: 'json'
        });

    }

    dataURItoBlob(dataURI) {
        // convert base64 to raw binary data held in a string
        // doesn't handle URLEncoded DataURIs - see SO answer #6850276 for code that does this
        let byteString = atob(dataURI.split(',')[1]);

        // separate out the mime component
        let mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0]

        // write the bytes of the string to an ArrayBuffer
        let ab = new ArrayBuffer(byteString.length);
        let ia = new Uint8Array(ab);
        for (let i = 0; i < byteString.length; i++) {
            ia[i] = byteString.charCodeAt(i);
        }

        // write the ArrayBuffer to a blob, and you're done
        let blob = new Blob([ab], {type: mimeString});
        return blob;

        // Old code
        // var bb = new BlobBuilder();
        // bb.append(ab);
        // return bb.getBlob(mimeString);
    }



    imageChanged(picture) {
        this.setState({picture});
    }

    submitForm(){
        let data = new FormData();
        const {title, description, price, picture} = this.state;
        data.append("title", title);
        data.append("description", description);
        data.append("price", price);
        if(picture) data.append("image",this.dataURItoBlob(picture));

        this.client.post("/check", data)
    }

    render() {
        return <div className="container">
            <div className="row">
                <form className="col-md-4">
                    <div className="form-group">
                        <label for="exampleInputEmail1">Product title</label>
                        <input type="email" className="form-control"
                               onChange={data => this.setState({title: data.target.value})}
                               id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="Enter email"/>
                    </div>
                    <div className="form-group">
                        <label for="exampleInputEmail1">Product description</label>
                        <input type="email" className="form-control"
                               onChange={data => this.setState({description: data.target.value})}
                               id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="Enter email"/>
                    </div>
                    <div className="form-group">
                        <label for="exampleInputEmail1">Price</label>

                        <input type="email" className="form-control"
                               onChange={data => this.setState({price: data.target.value})}
                               id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="Enter email"/>
                    </div>
                    <div className="form-group">

                        <ImagePicker onChanged={(image) => this.imageChanged(image)}/>
                    </div>
                    <div className="form-group">

                        <div className="btn btn-success" onClick={() => this.submitForm()}>Submit form</div>
                    </div>
                </form>

            </div>

        </div>


    }

}