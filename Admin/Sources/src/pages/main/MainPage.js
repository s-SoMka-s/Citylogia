import React, { Component } from 'react'
import PropTypes from 'prop-types'
import './MainPage.scss'
import UsersIcon from '../../assets/users-icon.png'
import ReviewsIcon from '../../assets/ReviewsIcon.png'
import PlacesIcon from '../../assets/PlacesIcon.png'
import ListIcon from '../../assets/ListIcon.png'
import InfoBlock from './InfoBlock/InfoBlock'
import MapImg from '../../assets/map.png'

export class MainPage extends Component {
    static propTypes = {}

    render() {
        return (
            <div className="main-page">
                <h1>Main page works</h1>
            </div>
        )
    }
}

export default MainPage
