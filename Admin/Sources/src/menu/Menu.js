import React, { Component } from 'react'
import { Link } from 'react-router-dom'
import PropTypes from 'prop-types'
import './Menu.scss'
import CitylogiaIcon from '../assets/CitylogiaIcon.svg'
import MainPageIcon from '../assets/MainPageIcon.svg'
import PlacesPageIcon from '../assets/PlacesPageIcon.svg'
import UsersPageIcon from '../assets/UsersPageIcon.svg'
import ReviwsPageIcon from '../assets/ReviewsPageIcon.svg'
import LineIcon from '../assets/Line.svg'
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
                <nav>
                    <ul>
                        <li className="menu-item">
                            <Link to="/">
                                <img src={CitylogiaIcon}></img>
                                <span>Citylogia</span>
                            </Link>
                        </li>
                        <img
                            className="menu-item__delimeter"
                            src={LineIcon}
                        ></img>
                        <li className="menu-item">
                            <Link to="/">
                                <img src={MainPageIcon}></img>
                                <span>Главная</span>
                            </Link>
                        </li>
                        <li className="menu-item">
                            <Link to="/places">
                                <img src={PlacesPageIcon}></img>
                                <span>Места</span>
                            </Link>
                        </li>
                        <li className="menu-item">
                            <Link to="/reviews">
                                <img src={ReviwsPageIcon}></img>
                                <span>Отзывы</span>
                            </Link>
                        </li>
                        <li className="menu-item">
                            <Link to="/users">
                                <img src={UsersPageIcon}></img>
                                <span>Пользователи</span>
                            </Link>
                        </li>
                    </ul>
                </nav>
            </div>
        )
    }
}

export default Menu
