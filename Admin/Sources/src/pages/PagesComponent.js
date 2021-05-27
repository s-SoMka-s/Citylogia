import React, { Component } from 'react'
import { Switch, Route, BrowserRouter } from 'react-router-dom'
import PropTypes from 'prop-types'
import ToolBar from '../components/toolbar/ToolBar'
import Menu from '../menu/Menu'
import PlacesPage from './places/PlacesPage'
import ReviewsPage from './reviews/ReviewsPage'
import UsersPage from './users/UsersPage'
import MainPage from './main/MainPage'
import './PagesComponent.scss'

export class PagesComponent extends Component {
    static propTypes = {}

    render() {
        return (
            <div className="pages">
                <div className="pages__sidebar">
                    <Menu></Menu>
                </div>
                <div className="pages__wrapper">
                    <div className="pages__header">
                        <ToolBar></ToolBar>
                    </div>

                    <div className="pages__content-box">
                        <Switch>
                            <Route
                                path="/pages/places"
                                component={PlacesPage}
                            />
                            <Route
                                path="/pages/reviews"
                                component={ReviewsPage}
                            />
                            <Route path="/pages/users" component={UsersPage} />
                            <Route path="/pages/" component={MainPage} />
                        </Switch>
                    </div>
                </div>
            </div>
        )
    }
}

export default PagesComponent
