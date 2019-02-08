
import * as React from "react"
import * as TestRenderer from "react-test-renderer"
import { SelectControl, IOptionsQueryResponse, ISelectionList } from "../SelectControl"
import { IFieldComponentProps } from "../props"
import { Grid, InputLabel, FormHelperText, Select } from "@material-ui/core"
import { MockApolloClient } from "../../../../../__testutils__/mockapollo"
import { ReadOnlyValue } from "../ReadOnlyValue"

jest.mock("../../../../../graphql/withApolloClient")
const setClient = require("../../../../../graphql/withApolloClient").setClient

describe("SelectControl", () => {

    let renderer: TestRenderer.ReactTestRenderer
    let instance: TestRenderer.ReactTestInstance
    const mockClient = new MockApolloClient()

    function getComponentProps(): IFieldComponentProps {
        return {
            meta: null as any,
            entity: null as any,
            field: {
                name: "select",
                label: "Field Label",
                type: "SelectField",
                nullable: true,
                readonly: false,
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
            readonly: false,
            style: { marginTop: 10 },
            onChange: jest.fn(),
            children: null
        }
    }

    const selectionList: ISelectionList = {
        code: "account_sources",
        label: "Account Sources",
        options: [
            { code: "option1", label: "Option 1" },
            { code: "option2", label: "Option 2" },
            { code: "option3", label: "Option 3" }
        ]
    }

    beforeAll(() => {
        mockClient.setQueryResult<IOptionsQueryResponse>({
            data: {
                SelectionList: {
                    results: [selectionList]
                }
            }
        })
        setClient(mockClient)
    })

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
            const select = instance.findByType(Select)
            expect(select).toBeDefined()
            expect(select.props.inputProps.id).toEqual(props.field.name)
            expect(select.props.value).toEqual(props.value)
            expect(select.props.disabled).toEqual(props.disabled)
            expect(select.props.error).toEqual(false)
        })

        it("Select component has correct options", () => {
            const select = instance.findByType(Select)
            const blankOpt = select.props.children[0]
            expect(blankOpt.props.value).toEqual("")
            // expect(blankOpt.props.children).to.be.undefined
            const selectionOpts = select.props.children[1]
            expect(selectionOpts.length).toEqual(selectionList.options.length)
            expect(selectionOpts[0].props.value).toEqual("option1")
            expect(selectionOpts[0].props.children).toEqual("Option 1")
            expect(selectionOpts[1].props.value).toEqual("option2")
            expect(selectionOpts[1].props.children).toEqual("Option 2")
        })

        it("does not render form helper text when there are no errors", () => {
            const helpSelect = instance.findAllByType(FormHelperText)
            expect(helpSelect.length).toEqual(0)
        })

        it("applies passed-in style", () => {
            const outerDiv = instance.findAll(el => el.type == "div")[0]
            expect(outerDiv.props.style).toMatchObject({ marginTop: 10 })
        })

    })

    describe("readonly mode", () => {
        const props = getComponentProps()
        props.readonly = true

        beforeAll(() => {
            render(props)
        })

        it("renders an InputLabel component containing props.label and no error", () => {
            const label = instance.findByType(InputLabel)
            expect(label).toBeDefined()
            expect(label.props.children).toEqual(props.label)
            expect(label.props.children).not.toEqual(props.field.label)
            expect(label.props.error).toEqual(false)
        })

        it("Does not render an Input component with expected props", () => {
            const select = instance.findAllByType(Select)
            expect(select.length).toEqual(0)
        })

        it("Renders the ReadOnlyValue component with expected props", () => {
            const component = instance.findByType(ReadOnlyValue)
            expect(component).toBeDefined()
            expect(component.props.children).toEqual(props.value)
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
