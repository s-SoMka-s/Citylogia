import React, { Component } from 'react'
import { Link } from 'react-router-dom'
import PropTypes from 'prop-types'
import './MainPage.scss'
import UsersIcon from '../../assets/users-icon.png'
import ReviewsIcon from '../../assets/ReviewsIcon.png'
import PlacesIcon from '../../assets/PlacesIcon.png'
import ListIcon from '../../assets/ListIcon.png'
import InfoBlock from './InfoBlock/InfoBlock'
import MapImg from '../../assets/map.png'
import LineInputField from '../../components/line-input-field/LineInputField'
import ToolBar from '../../components/toolbar/ToolBar'
import PlusIcon from '../../assets/Plus.svg'

export class MainPage extends Component {
    static propTypes = {}

    render() {
        return (
            <div className="main-page-container">
                <div className="pages-header">
                    <ToolBar></ToolBar>
                </div>
                <div className="main-page">
                    <InfoBlock
                        icon={UsersIcon}
                        count={62}
                        title={'Пользователей'}
                    ></InfoBlock>
                    <InfoBlock
                        icon={ReviewsIcon}
                        count={134}
                        title={'Отзывов'}
                    ></InfoBlock>
                    <InfoBlock
                        icon={PlacesIcon}
                        count={25}
                        title={'Мест'}
                    ></InfoBlock>
                    <InfoBlock
                        icon={ListIcon}
                        count={180}
                        title={'Запросов'}
                    ></InfoBlock>
                    {/* <div className="main-page__map"></div> */}
                    <div className="main-page__form">
                        <label htmlFor="nameInput">Название</label>
                        <input id="nameInput" placeholder="Название"></input>
                        <div className="main-page__form-divider"></div>
                        <label htmlFor="shortDescriptionInput">
                            Краткое описание
                        </label>
                        <input
                            id="shortDescriptionInput"
                            placeholder="Краткое описание"
                        ></input>
                        <div className="main-page__form-divider"></div>
                        <label htmlFor="descriptionInput">Описание</label>
                        <input
                            id="descriptionInput"
                            placeholder="Описание"
                        ></input>
                        <div className="main-page__form-divider"></div>
                        <label htmlFor="typeInput">Тип места</label>
                        <input id="typeInput" placeholder="Тип места"></input>
                        <div className="main-page__form-divider"></div>
                        <label htmlFor="cityInput">Адресс</label>
                        <input id="cityInput" placeholder="Город"></input>
                        <input id="streetInput" placeholder="Улица"></input>
                        <input id="houseInput" placeholder="Дом"></input>
                        <div className="main-page__form-divider"></div>
                        <label htmlFor="coordInput">Координаты</label>
                        <input id="coordInput" placeholder="Долгота"></input>
                        <input id="coord2Input" placeholder="Широта"></input>
                        <div className="main-page__form-divider"></div>
                        <div className="main-page__form-photos-controls">
                            <span>Фотки</span>
                            <img src={PlusIcon}></img>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default MainPage
