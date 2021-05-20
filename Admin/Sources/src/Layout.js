import React from 'react'
import { Switch, Route, BrowserRouter } from 'react-router-dom'
import Menu from './menu/Menu'
import MainPageComponent from './pages/main/MainPage'
import AuthPageComponent from './pages/auth/AuthPage'
import UsersPageComponent from './pages/users/UsersPage'
import ReviewsPageComponent from './pages/reviews/ReviewsPage'
import PlacesPageCompoenent from './pages/places/PlacesPage'
import './Layout.scss'

export default function Layout() {
    return (
        <div className="app">
            <div className="app__sidebar">
                <Menu></Menu>
            </div>
            <div className="app__wrapper">
                <header>
                    <h1>
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                        Curabitur lectus sem, pulvinar ut condimentum eget,
                        faucibus sed nulla. Phasellus blandit metus quis dui
                        accumsan, et rhoncus ex pretium
                    </h1>
                </header>

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
