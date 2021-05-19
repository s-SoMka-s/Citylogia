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
                <div className="main-page-info-block__container">
                    <div className="main-page-info-block__icon">
                        <img src={this.props.icon} alt="users-icon"></img>
                    </div>
                    <div className="main-page-info-block__text">
                        <p className="main-page-info-block__text-title">
                            {this.props.count}
                        </p>
                        <p className="main-page-info-block__text-subtitle">
                            {this.props.subtitle}
                        </p>
                    </div>
                </div>
            </div>
        )
    }
}

export default InfoBlock
