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

export class MainPage extends Component {
    static propTypes = {}

    render() {
        return (
            <div className="main-page-container">
                <div className="pages__header">
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
                        <LineInputField></LineInputField>
                    </div>
                </div>
            </div>
        )
    }
}

export default MainPage
