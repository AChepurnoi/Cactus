/**
 * Created by Sasha on 5/14/17.
 */


/**
 * Created by Sasha on 5/14/17.
 */

import React from 'react'


export default class SelectableLable extends React.Component {

    constructor(props) {
        super(props);
        this.state = {selected: false}
    }

    render() {
        return <span
            className={"col-sm-8 col-sm-offset-2 image-label label " + (this.state.selected ? "label-success" : "label-warning")}
            onClick={() => this.setState({selected: !this.state.selected})}>{this.props.text}</span>

    }

}