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

export default function Layout() {
    return (
        <div className="app">
            <div className="app__sidebar">
                <Menu></Menu>
            </div>
            <div className="app__wrapper">
                <div className="app__header">
                    <ToolBar></ToolBar>
                </div>

                <div className="app__content-box">
                    <Switch>
                        <Route path="/auth" component={AuthPageComponent} />
                        <Route
                            path="/places"
                            component={PlacesPageCompoenent}
                        />
                        <Route
                            path="/reviews"
                            component={ReviewsPageComponent}
                        />
                        <Route path="/users" component={UsersPageComponent} />
                        <Route path="/" component={MainPageComponent} />
                    </Switch>
                </div>
            </div>
        </div>
    )
}
