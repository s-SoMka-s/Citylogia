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
                <div className="main-page__container">
                    {/* <div class="main-page__info-container">
                        <InfoBlock></InfoBlock>
                        <InfoBlock></InfoBlock>
                        <InfoBlock></InfoBlock>
                        <InfoBlock></InfoBlock>
                    </div> */}
                </div>
            </div>
        )
    }
}

export default MainPage
