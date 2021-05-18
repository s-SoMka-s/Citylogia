import React, { Component } from 'react'
import PropTypes from 'prop-types'
import './MainPage.scss'
import UsersIcon from '../../assets/users-icon.png'
import ReviewsIcon from '../../assets/ReviewsIcon.png'
import PlacesIcon from '../../assets/PlacesIcon.png'
import ListIcon from '../../assets/ListIcon.png'

export class MainPage extends Component {
    static propTypes = {}

    render() {
        return (
            <div className="main-page">
                <div className="main-page__container">
                    <div className="main-page__info">
                        <div className="main-page__info-item"></div>
                        <div className="main-page__info-item"></div>
                        <div className="main-page__info-item"></div>
                        <div className="main-page__info-item"></div>
                    </div>
                    <div className="main-page__map"></div>
                </div>
            </div>
        )
    }
}

export default MainPage
