import React from 'react'
import { Switch, Route, BrowserRouter } from 'react-router-dom'
import Menu from './menu/Menu'
import MainPageComponent from './pages/main/MainPage'
import AuthPageComponent from './pages/auth/AuthPage'
import UsersPageComponent from './pages/users/UsersPage'
import ReviewsPageComponent from './pages/reviews/ReviewsPage'
import PlacesPageCompoenent from './pages/places/PlacesPage'
import './Layout.scss'
import ToolBar from './components/toolbar/ToolBar'
import PagesComponent from './pages/PagesComponent'

export default function Layout() {
    return (
        <div className="app">
            <Switch>
                <Route path="/auth" component={AuthPageComponent} />
                <Route path="/pages" component={PagesComponent}></Route>
            </Switch>
        </div>
    )
}
