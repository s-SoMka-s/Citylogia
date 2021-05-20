import React, { Component } from 'react'
import PropTypes from 'prop-types'
import './InfoBlock.scss'
import UsersIcon from '../../../assets/users-icon.png'
import ReviewsIcon from '../../../assets/ReviewsIcon.png'
import PlacesIcon from '../../../assets/PlacesIcon.png'
import ListIcon from '../../../assets/ListIcon.png'

export class InfoBlock extends Component {
    static propTypes = {}

    render() {
        return (
            <div className="main-page-info-block">
                <img src={this.props.icon}></img>
            </div>
        )
    }
}

export default InfoBlock
