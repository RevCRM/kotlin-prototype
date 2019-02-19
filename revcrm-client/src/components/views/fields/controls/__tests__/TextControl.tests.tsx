
import * as React from "react"
import * as TestRenderer from "react-test-renderer"
import { TextControl } from "../TextControl"
import { IFieldComponentProps } from "../props"
import { Grid, InputLabel, Input, FormHelperText } from "@material-ui/core"
import { ReadOnlyValue } from "../ReadOnlyValue"

describe("TextControl", () => {

    let renderer: TestRenderer.ReactTestRenderer
    let instance: TestRenderer.ReactTestInstance

    function getComponentProps(): IFieldComponentProps {
        return {
            meta: null as any,
            entity: null as any,
            field: {
                name: "text",
                label: "Field Label",
                type: "TextField",
                nullable: true,
                readonly: false,
                properties: {},
                constraints: {}
            },
            label: "Field Component Label",
            grid: {
                colspanNarrow: 12,
                colspan: 6,
                colspanWide: 4,
            },
            value: "some value",
            errors: [],
            disabled: false,
            readonly: false,
            style: { marginTop: 10 },
            onChange: jest.fn(),
            children: null
        }
    }

    function render(props: IFieldComponentProps) {
        renderer = TestRenderer.create(
            <TextControl {...props} />
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
            expect(grid.props.xs).toEqual(props.grid!.colspanNarrow)
            expect(grid.props.md).toEqual(props.grid!.colspan)
            expect(grid.props.lg).toEqual(props.grid!.colspanWide)
        })

        it("renders an InputLabel component containing props.label and no error", () => {
            const label = instance.findByType(InputLabel)
            expect(label).toBeDefined()
            expect(label.props.children).toEqual(props.label)
            expect(label.props.children).not.toEqual(props.field.label)
            expect(label.props.error).toEqual(false)
        })

        it("Renders an Input component with expected props", () => {
            const input = instance.findByType(Input)
            expect(input).toBeDefined()
            expect(input.props.id).toEqual(props.field.name)
            expect(input.props.type).toEqual("text")
            expect(input.props.value).toEqual(props.value)
            expect(input.props.disabled).toEqual(props.disabled)
            expect(input.props.error).toEqual(false)
        })

        it("does not render form helper text when there are no errors", () => {
            const helpText = instance.findAllByType(FormHelperText)
            expect(helpText.length).toEqual(0)
        })

        it("applies passed-in style", () => {
            const outerDiv = instance.findAll(el => el.type == "div")[0]
            expect(outerDiv.props.style).toMatchObject({ marginTop: 10 })
        })

    })

    describe("multiline text entry", () => {

        it("not multi-line by default", () => {
            const props = getComponentProps()
            render(props)

            const input = instance.findByType(Input)
            expect(input).toBeDefined()
            expect(input.props.multiline).toBeFalsy()
        })

        it("when properties.MultiLine == true, input is multi-line", () => {
            const props = getComponentProps()
            props.field.properties.MultiLine = "true"
            render(props)

            const input = instance.findByType(Input)
            expect(input).toBeDefined()
            expect(input.props.multiline).toEqual(true)
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
            const input = instance.findAllByType(Input)
            expect(input.length).toEqual(0)
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

        it("Input component has the error property set", () => {
            const input = instance.findByType(Input)
            expect(input).toBeDefined()
            expect(input.props.error).toEqual(true)
        })

        it("renders form helper text with error property set", () => {
            const helpText = instance.findByType(FormHelperText)
            expect(helpText).toBeDefined()
            expect(helpText.props.error).toEqual(true)
        })

        it("error messages are concetenated", () => {
            const helpText = instance.findByType(FormHelperText)
            expect(helpText.props.children).toEqual("Error 1. Error 2. ")
        })

    })

})
