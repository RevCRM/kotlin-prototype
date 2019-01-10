
import * as React from "react"
import * as TestRenderer from "react-test-renderer"
import { SelectControl } from "../SelectControl"
import { IFieldComponentProps } from "../props"
import { Grid, InputLabel, FormHelperText, Select } from "@material-ui/core"

describe("SelectControl", () => {

    let renderer: TestRenderer.ReactTestRenderer
    let instance: TestRenderer.ReactTestInstance

    function getComponentProps(): IFieldComponentProps {
        return {
            field: {
                name: "select",
                label: "Field Label",
                type: "SelectField",
                nullable: true,
                properties: {},
                constraints: {
                    SelectionList: "account_sources"
                }
            },
            label: "Field Component Label",
            colspanNarrow: 12,
            colspan: 6,
            colspanWide: 4,
            value: "some value",
            errors: [],
            disabled: false,
            style: {marginTop: 10},
            onChange: jest.fn()
        }
    }

    function render(props: IFieldComponentProps) {
        renderer = TestRenderer.create(
            <SelectControl {...props} />
        )
        instance = renderer.root
    }

    describe("basic rendering", () => {
        const props = getComponentProps()

        beforeAll(() => {
            render(props)
        })

        it("renders a Grid component with \"item\" property set", () => {
            const grid = instance.findByType(Grid)
            expect(grid).toBeDefined()
            expect(grid.props.item).toBeTruthy()
        })

        it("Grid component has column widths set correctly", () => {
            const grid = instance.findByType(Grid)
            expect(grid).toBeDefined()
            expect(grid.props.xs).toEqual(props.colspanNarrow)
            expect(grid.props.md).toEqual(props.colspan)
            expect(grid.props.lg).toEqual(props.colspanWide)
        })

        it("renders an InputLabel component containing props.label and no error", () => {
            const label = instance.findByType(InputLabel)
            expect(label).toBeDefined()
            expect(label.props.children).toEqual(props.label)
            expect(label.props.children).not.toEqual(props.field.label)
            expect(label.props.error).toEqual(false)
        })

        it("Renders a Select component with expected props", () => {
            const input = instance.findByType(Select)
            expect(input).toBeDefined()
            expect(input.props.inputProps.id).toEqual(props.field.name)
            expect(input.props.value).toEqual(props.value)
            expect(input.props.disabled).toEqual(props.disabled)
            expect(input.props.error).toEqual(false)
        })

        it("does not render form helper text when there are no errors", () => {
            const helpSelect = instance.findAllByType(FormHelperText)
            expect(helpSelect.length).toEqual(0)
        })

        it("applies passed-in style", () => {
            const outerDiv = instance.findAll(el => el.type == "div")[0]
            expect(outerDiv.props.style).toEqual({marginTop: 10})
        })

    })

    describe("when the errors property contains items", () => {

        beforeAll(() => {
            const props = getComponentProps()
            props.errors = [
                { message: "Error 1" },
                { message: "Error 2" }
            ]
            render(props)
        })

        it("InputLabel component has the error property set", () => {
            const label = instance.findByType(InputLabel)
            expect(label).toBeDefined()
            expect(label.props.error).toEqual(true)
        })

        it("Select component has the error property set", () => {
            const input = instance.findByType(Select)
            expect(input).toBeDefined()
            expect(input.props.error).toEqual(true)
        })

        it("renders form helper text with error property set", () => {
            const helpSelect = instance.findByType(FormHelperText)
            expect(helpSelect).toBeDefined()
            expect(helpSelect.props.error).toEqual(true)
        })

        it("error messages are concetenated", () => {
            const helpSelect = instance.findByType(FormHelperText)
            expect(helpSelect.props.children).toEqual("Error 1. Error 2. ")
        })

    })

})
