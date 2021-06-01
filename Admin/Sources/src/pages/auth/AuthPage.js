import React, { Component } from 'react'
import './AuthPage.scss'
import Button from '@material-ui/core/Button'
import { ApiService } from '../../Interceptors/ApiService'

export class AuthPage extends Component {
    constructor(props) {
        super(props)
        this.state = {}
        // This line is important!
        this.login = this.login.bind(this)
        this.onInputchange = this.onInputchange.bind(this)
    }

    static propTypes = {}

    onInputchange(event) {
        this.setState({
            [event.target.name]: event.target.value,
        })
    }

    login() {
        let api = new ApiService()
        let data = {
            email: this.state.email,
            password: this.state.password,
        }

        return api.login(data).then((res) => this.props.history.push('/pages'))
    }

    render() {
        return (
            <div className="auth-page">
                <link
                    rel="stylesheet"
                    href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.1.3/css/bootstrap.min.css"
                />
                <div className="auth-page__form">
                    <h1>Авторизация</h1>
                    <p>Для получения аккаунта свяжитесь с администратором</p>

                    <Button onClick={this.login} variant="outlined">
                        Авторизоваться
                    </Button>
                </div>
            </div>
        )
    }
}

export default AuthPage
