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
                    <div className="main-page__info-container">
                        <InfoBlock
                            icon={UsersIcon}
                            count={62}
                            subtitle={'Пользователей'}
                        ></InfoBlock>
                        <InfoBlock
                            icon={ReviewsIcon}
                            count={134}
                            subtitle={'Отзывов'}
                        ></InfoBlock>
                        <InfoBlock
                            icon={PlacesIcon}
                            count={25}
                            subtitle={'Мест'}
                        ></InfoBlock>
                        <InfoBlock
                            icon={ListIcon}
                            count={180}
                            subtitle={'Запросов'}
                        ></InfoBlock>
                    </div>
                    <div className="main-page__map-container">
                        <div className="main-page__map">
                            <img src={MapImg}></img>
                        </div>
                        <div className="main-page__form"></div>
                    </div>
                </div>
            </div>
        )
    }
}

export default MainPage
