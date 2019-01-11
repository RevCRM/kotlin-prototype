import React from "react"
import Autosuggest, { SuggestionSelectedEventData, SuggestionHighlightedParams } from "react-autosuggest"
import match from "autosuggest-highlight/match"
import parse from "autosuggest-highlight/parse"
import Paper from "@material-ui/core/Paper"
import MenuItem from "@material-ui/core/MenuItem"
import { withStyles, Theme, createStyles, WithStyles } from "@material-ui/core/styles"
import { IFieldComponentProps } from "./props"
import { Input, Grid, FormControl, InputLabel, InputAdornment, Icon } from "@material-ui/core"
import { getGridWidthProps, IMUIGridProps } from "../../views/Grid"
import { ISelectionOption, IOptionsQueryResponse } from "./SelectControl"
import gql from "graphql-tag"
import { IApolloClientProp, withApolloClient } from "../../../graphql/withApolloClient"
import { LoadState } from "../../utils/types"

const OPTIONS_QUERY = gql`
    query ($code: String!) {
        SelectionList ( where: { code: $code }) {
            results {
                code
                label
                options {
                    code
                    label
                }
            }
        }
    }
`

export const styles = (theme: Theme) => createStyles({
    container: {
        position: "relative",
    },
    suggestionsContainerOpen: {
        position: "absolute",
        zIndex: 1,
        left: 0,
        right: 0,
    },
    suggestion: {
        display: "block",
    },
    suggestionsList: {
        margin: 0,
        padding: 0,
        listStyleType: "none",
    },
    divider: {
        height: theme.spacing.unit * 2,
    },
})

export interface ISearchSelectControlProps extends
                                IFieldComponentProps,
                                IApolloClientProp,
                                WithStyles<typeof styles> {
}

export interface ISearchSelectControlState {
    loadState: LoadState
    options: ISelectionOption[]
    value: ISelectionOption | null
    search: string
    suggestions: any[]
    hasMore: boolean
}

export const MAX_RESULTS = 8

export const SearchSelectControl: any = withStyles(styles)(withApolloClient(
    class extends React.Component<ISearchSelectControlProps, ISearchSelectControlState> {
    gridWidthProps: IMUIGridProps

    constructor(props: any) {
        super(props)
        this.gridWidthProps = getGridWidthProps(props)
        this.state = {
            loadState: "not_loaded",
            options: [],
            value: null,
            search: "",
            suggestions: [],
            hasMore: false
        }
    }

    async initialise() {
        this.setState({ loadState: "loading" })
        const res = await this.props.client.query<IOptionsQueryResponse>({
            query: OPTIONS_QUERY,
            variables: {
                code: this.props.field.constraints.SelectionList
            }
        })
        if (res.errors && res.errors.length) {
            this.setState({ loadState: "load_error" })
            console.error("Failed to load options", res.errors)
        }
        else {
            console.log("options loaded", res.data)
            this.setState({
                loadState: "loaded",
                options: res.data.SelectionList.results[0].options
            })
        }
    }

    async componentDidMount() {
        this.initialise()
    }

    handleSuggestionsFetchRequested = (input: any) => {
        const inputValue = input.value.trim().toLowerCase()
        const matches = this.state.options.filter(item => (
            inputValue == "" || item.label.toLowerCase().includes(inputValue)
        ))
        const suggestions = matches.slice(0, MAX_RESULTS)
        const hasMore = matches.length > MAX_RESULTS

        this.setState({ suggestions, hasMore })
    }

    shouldRenderSuggestions = (inputValue: any) => {
        return true
    }

    handleInputClearRequested = () => {
        this.setState({ value: null, search: "" })
    }

    handleSuggestionsClearRequested = () => {
        this.setState({ suggestions: [] })
        this.props.onChange(null)
    }

    handleSearchChange = (event: any, target: any) => {
        this.setState({ search: target.newValue })
    }

    onSuggestionSelected = (event: any, data: SuggestionSelectedEventData<ISelectionOption>) => {
        this.setState({ value: data.suggestion })
        this.props.onChange(data.suggestion.code)
    }

    onSuggestionHighlighted = (params: SuggestionHighlightedParams) => {
        if (params.suggestion) {
            this.setState({ value: params.suggestion })
            this.props.onChange(params.suggestion.code)
        }
    }

    renderInputComponent = (inputProps: any) => {
        const { classes, ref, inputRef = () => null, ...otherProps } = inputProps
        const fieldId = this.props.field.name
        const hasErrors = this.props.errors.length > 0
        return (
            <FormControl fullWidth>
                <InputLabel
                    htmlFor={fieldId}
                    error={hasErrors}
                    shrink={true}
                >
                    {this.props.label}
                </InputLabel>
                <Input
                    fullWidth
                    inputRef={(node) => {
                        ref(node)
                        inputRef(node)
                    }}
                    endAdornment={
                        <InputAdornment position="end">
                            {this.state.value &&
                                <Icon fontSize="small" style={{ cursor: "pointer" }}
                                    onClick={this.handleInputClearRequested}
                                >clear</Icon>}
                            {!this.state.value &&
                                <Icon fontSize="small" style={{ cursor: "pointer" }}
                                    onClick={() => alert("drop down thing")}
                            >arrow_drop_down</Icon>}
                        </InputAdornment>
                    }
                    {...otherProps}
                />
            </FormControl>
        )
    }

    getSuggestionValue(suggestion: any) {
        return suggestion.label
    }

    renderSuggestion = (suggestion: any, props: any) => {
        const matches = match(suggestion.label, props.query)
        const parts = parse(suggestion.label, matches)
        return (
            <MenuItem dense selected={props.isHighlighted} component="div">
                <div>
                    {parts.map((part, index) => {
                    return part.highlight ? (
                        <span key={String(index)} style={{ fontWeight: 500 }}>
                        {part.text}
                        </span>
                    ) : (
                        <strong key={String(index)} style={{ fontWeight: 300 }}>
                        {part.text}
                        </strong>
                    )
                    })}
                </div>
            </MenuItem>
        )
    }

    render() {
        const { classes } = this.props as any
        return (
            <Grid item {...this.gridWidthProps} style={this.props.style}>
                <Autosuggest
                    renderInputComponent={this.renderInputComponent}
                    renderSuggestion={this.renderSuggestion}
                    shouldRenderSuggestions={this.shouldRenderSuggestions}
                    getSuggestionValue={this.getSuggestionValue}
                    inputProps={{
                        value: this.state.search,
                        onChange: this.handleSearchChange
                    }}
                    theme={{
                        container: classes.container,
                        suggestionsContainerOpen: classes.suggestionsContainerOpen,
                        suggestionsList: classes.suggestionsList,
                        suggestion: classes.suggestion,
                    }}
                    renderSuggestionsContainer={options => (
                        <Paper {...options.containerProps} square>
                            {options.children}
                            {this.state.hasMore && <MenuItem dense>
                                <em style={{ fontWeight: 300 }}>
                                    More...
                                </em>
                            </MenuItem>}
                        </Paper>
                    )}
                    suggestions={this.state.suggestions}
                    onSuggestionsFetchRequested={this.handleSuggestionsFetchRequested}
                    onSuggestionsClearRequested={this.handleSuggestionsClearRequested}
                    onSuggestionSelected={this.onSuggestionSelected}
                    onSuggestionHighlighted={this.onSuggestionHighlighted}
                />
            </Grid>
        )
    }
}))
