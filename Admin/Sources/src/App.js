import logo from './logo.svg'
import './App.css'
import MainPage from './pages/main/MainPage'
import Menu from './menu/Menu'

function App() {
    return (
        <div className="App">
            <Menu></Menu>
            <div className="wrapper">
                <div className="toolbar"></div>
                <div className="page-container">
                    <MainPage></MainPage>
                </div>
            </div>
        </div>
    )
}

export default App
