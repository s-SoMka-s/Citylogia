import React from 'react'
import './PlacesPlage.scss'
import { makeStyles } from '@material-ui/core/styles'
import Table from '@material-ui/core/Table'
import TableBody from '@material-ui/core/TableBody'
import TableCell from '@material-ui/core/TableCell'
import TableContainer from '@material-ui/core/TableContainer'
import TableHead from '@material-ui/core/TableHead'
import TableRow from '@material-ui/core/TableRow'
import Paper from '@material-ui/core/Paper'
import Tabs from '@material-ui/core/Tabs'
import Tab from '@material-ui/core/Tab'
import Button from '@material-ui/core/Button'
import DeleteIcon from '@material-ui/icons/AddOutlined'

import LeftArrow from '../../assets/pagination/arrow-left.svg'
import RightArrow from '../../assets/pagination/arrow-right.svg'
import { ApiService } from '../../Interceptors/ApiService'

const useStyles = makeStyles({})

const statuses = {
    NOT_REVIEWED: 1,
    APPROVED: 2,
}

export default function PlacesPage() {
    let api = new ApiService()
    const [value, setValue] = React.useState(0)
    const [places, setPlaces] = React.useState(new Array())

    const handleChange = (event, newValue) => {
        setValue(newValue)

        let take = 7
        let skip = 0
        let onlyApproved = false
        let onlyNotReviewed = false

        if (newValue == 0) {
            onlyApproved = true
        } else {
            onlyNotReviewed = true
        }

        api.takePlaces(take, skip, onlyApproved, onlyNotReviewed).then(
            (places) => {
                setPlaces(places.elements)
            }
        )
    }

    return (
        <div className="places-page">
            <h1>Места</h1>
            <div className="places-page__controls">
                <Paper className="places-page__controls-tabs">
                    <Tabs
                        value={value}
                        onChange={handleChange}
                        indicatorColor="primary"
                        textColor="primary"
                    >
                        <Tab label="Подтвержденные" />
                        <Tab label="Ждут подтверждения" />
                    </Tabs>
                </Paper>
                <Button
                    className="places-page__controls-button"
                    variant="contained"
                    color="secondary"
                    startIcon={<DeleteIcon />}
                >
                    Добавить
                </Button>
            </div>
            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell align="left">Предложил</TableCell>
                            <TableCell align="left">Название места</TableCell>
                            <TableCell align="left">Тип места</TableCell>
                            <TableCell align="left">Адрес</TableCell>
                            <TableCell align="left">Статус</TableCell>
                            <TableCell align="left">Операции</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {places.map((place) => (
                            <TableRow key={place.id}>
                                <TableCell
                                    align="left"
                                    component="th"
                                    scope="row"
                                >
                                    {place.author}
                                </TableCell>
                                <TableCell align="left">{place.name}</TableCell>
                                <TableCell align="left">
                                    {place.type.name}
                                </TableCell>
                                <TableCell align="left">
                                    {place.street}
                                </TableCell>
                                <TableCell align="left">
                                    {place.status == statuses.APPROVED ? (
                                        <div className="places-page__table-mark places-page__table-mark-approved">
                                            Подтвержден
                                        </div>
                                    ) : (
                                        <div className="places-page__table-mark places-page__table-mark-not-reviwed">
                                            Запрос
                                        </div>
                                    )}
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
            <div className="places-page__pagination">
                <img src={LeftArrow}></img>
                <div className="places-page__pagination-number">
                    <span>4</span>
                </div>
                <span>из</span>
                <div className="places-page__pagination-number">
                    <span>4</span>
                </div>
                <img src={RightArrow}></img>
            </div>
        </div>
    )
}
