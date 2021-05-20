import React, { Component } from 'react'
import PropTypes from 'prop-types'
import './Menu.scss'
import CitylogiaIcon from '../assets/CitylogiaIcon.svg'
import MainPageIcon from '../assets/MainPageIcon.svg'
import PlacesPageIcon from '../assets/PlacesPageIcon.svg'
import UsersPageIcon from '../assets/UsersPageIcon.svg'
import ReviwsPageIcon from '../assets/ReviewsPageIcon.svg'
import Logout from '../assets/Logout.svg'

const items = [
    {
        path: '/',
        text: 'Главная',
        icon: MainPageIcon,
    },
    {
        path: '/places',
        text: 'Места',
        icon: PlacesPageIcon,
    },
    {
        path: '/reviews',
        text: 'Отзывы',
        icon: ReviwsPageIcon,
    },
    {
        path: '/users',
        text: 'Пользователи',
        icon: UsersPageIcon,
    },
]

export class Menu extends Component {
    static propTypes = {}

    render() {
        return (
            <div className="menu">
                <div className="menu__container">
                    <div className="menu__logo">
                        <img src={CitylogiaIcon}></img>
                        <p>Citylogia</p>
                    </div>
                    <hr></hr>
                    <div className="menu__item">
                        <img src={MainPageIcon}></img>
                        <p>Главная</p>
                    </div>
                    <div className="menu__item">
                        <img src={PlacesPageIcon}></img>
                        <p>Места</p>
                    </div>
                    <div className="menu__item">
                        <img src={ReviwsPageIcon}></img>
                        <p>Отзывы</p>
                    </div>
                    <div className="menu__item">
                        <img src={UsersPageIcon}></img>
                        <p>Пользователи</p>
                    </div>
                    <div className="menu__item logout">
                        <img src={Logout}></img>
                        <p>Выйти</p>
                    </div>
                </div>
            </div>
        )
    }
}

export default Menu
