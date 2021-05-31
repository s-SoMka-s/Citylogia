import React, { Component } from 'react'
import PropTypes from 'prop-types'
import SeachIcon from '../../assets/search.svg'
import VerticalLine from '../../assets/VerticalLine.svg'
import Avatar from '../../assets/avatar.png'
import NotificationsIcon from '../../assets/notifications.svg'
import LogoutIcon from '../../assets/Logout.svg'
import './ToolBar.scss'

export class ToolBar extends Component {
    static propTypes = {}

    render() {
        return (
            <div className="toolbar">
                <div className="toolbar__search">
                    <img src={SeachIcon}></img>
                    <span>Поиск...</span>
                </div>
                <div className="toolbar__controls">
                    <div className="toolbar__controls-item">
                        <span>En/ru</span>
                    </div>
                    <img src={VerticalLine}></img>
                    <div className="toolbar__controls-item">
                        <div className="toolbar__controls-item-text">
                            <p className="toolbar__controls-item-text-title">
                                Игорь Чиёсов
                            </p>
                            <p className="toolbar__controls-item-text-subtitle">
                                Администратор
                            </p>
                        </div>
                        <img src={Avatar}></img>
                    </div>
                    <img src={VerticalLine}></img>
                    <div className="toolbar__controls-item">
                        <img src={NotificationsIcon}></img>
                        <img src={LogoutIcon}></img>
                    </div>
                </div>
            </div>
        )
    }
}

export default ToolBar
