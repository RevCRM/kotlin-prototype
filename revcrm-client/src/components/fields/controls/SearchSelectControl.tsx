import React from "react"
import Autosuggest from "react-autosuggest"
import match from "autosuggest-highlight/match"
import parse from "autosuggest-highlight/parse"
import TextField from "@material-ui/core/TextField"
import Paper from "@material-ui/core/Paper"
import MenuItem from "@material-ui/core/MenuItem"
import { withStyles } from "@material-ui/core/styles"

const suggestions = [
    { label: "Afghanistan" },
    { label: "Aland Islands" },
    { label: "Albania" },
    { label: "Algeria" },
    { label: "American Samoa" },
    { label: "Andorra" },
    { label: "Angola" },
    { label: "Anguilla" },
    { label: "Antarctica" },
    { label: "Antigua and Barbuda" },
    { label: "Argentina" },
    { label: "Armenia" },
    { label: "Aruba" },
    { label: "Australia" },
    { label: "Austria" },
    { label: "Azerbaijan" },
    { label: "Bahamas" },
    { label: "Bahrain" },
    { label: "Bangladesh" },
    { label: "Barbados" },
    { label: "Belarus" },
    { label: "Belgium" },
    { label: "Belize" },
    { label: "Benin" },
    { label: "Bermuda" },
    { label: "Bhutan" },
    { label: "Bolivia, Plurinational State of" },
    { label: "Bonaire, Sint Eustatius and Saba" },
    { label: "Bosnia and Herzegovina" },
    { label: "Botswana" },
    { label: "Bouvet Island" },
    { label: "Brazil" },
    { label: "British Indian Ocean Territory" },
    { label: "Brunei Darussalam" },
]

const styles = (theme: any) => ({
    root: {
    },
    container: {
        position: "relative",
    },
    suggestionsContainerOpen: {
        position: "absolute",
        zIndex: 1,
        marginTop: theme.spacing.unit,
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

class IntegrationAutosuggest extends React.Component {

    state = {
        single: "",
        popper: "",
        suggestions: [],
    }

    getSuggestions(value: any) {
        const inputValue = value.trim().toLowerCase()
        const inputLength = inputValue.length
        let count = 0
        return inputLength === 0
            ? []
            : suggestions.filter(suggestion => {
                const keep =
                    count < 10 && suggestion.label.toLowerCase().includes(inputValue)
                if (keep) count += 1
                return keep
            })
    }

    handleSuggestionsFetchRequested = (fetch: any) => {
        this.setState({ suggestions: this.getSuggestions(fetch.value) })
    }

    handleSuggestionsClearRequested = () => {
        this.setState({ suggestions: [] })
    }

    handleChange = (name: any) => (event: any, target: any) => {
        this.setState({ [name]: target.newValue })
    }

    renderInputComponent(inputProps: any) {
        const { classes, inputRef, ...otherProps } = inputProps
        return (
            <TextField
                fullWidth
                InputProps={{
                    ...otherProps,
                }}
            />
        )
    }

    getSuggestionValue(suggestion: any) {
        return suggestion.label
    }

    renderSuggestion(suggestion: any, props: any) {
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
            <div className={classes.root}>
                <Autosuggest
                    renderInputComponent={this.renderInputComponent}
                    renderSuggestion={this.renderSuggestion}
                    getSuggestionValue={this.getSuggestionValue}
                    inputProps={{
                        value: this.state.single,
                        onChange: this.handleChange("single"),
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
                        </Paper>
                    )}
                    suggestions={this.state.suggestions}
                    onSuggestionsFetchRequested={this.handleSuggestionsFetchRequested}
                    onSuggestionsClearRequested={this.handleSuggestionsClearRequested}
                />
            </div>
        )
    }
}

export const SearchSelectControl: any = withStyles(styles as any)(IntegrationAutosuggest)
