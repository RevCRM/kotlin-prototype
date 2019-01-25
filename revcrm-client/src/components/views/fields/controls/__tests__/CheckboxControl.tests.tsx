
import * as React from "react"
import * as TestRenderer from "react-test-renderer"
import { CheckboxControl } from "../CheckboxControl"
import { IFieldComponentProps } from "../props"
import { Grid, FormHelperText, Checkbox, FormControlLabel } from "@material-ui/core"

describe("CheckboxControl", () => {

    let renderer: TestRenderer.ReactTestRenderer
    let instance: TestRenderer.ReactTestInstance

    function getComponentProps(): IFieldComponentProps {
        return {
            meta: null as any,
            form: null as any,
            field: {
                name: "checkbox",
                label: "Field Label",
                type: "BooleanField",
                nullable: false,
                readonly: false,
                properties: {},
                constraints: {}
            },
            label: "Field Component Label",
            colspanNarrow: 12,
            colspan: 6,
            colspanWide: 4,
            value: true,
            errors: [],
            disabled: false,
            readonly: false,
            style: {marginTop: 10},
            onChange: jest.fn(),
            children: null
        }
    }

    function render(props: IFieldComponentProps) {
        renderer = TestRenderer.create(
            <CheckboxControl {...props} />
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

        it("renders an FormControlLabel component containing props.label and no error", () => {
            const label = instance.findByType(FormControlLabel)
            expect(label).toBeDefined()
            expect(label.props.label).toEqual(props.label)
            expect(label.props.label).not.toEqual(props.field.label)
        })

        it("Renders an Checkbox component with expected props", () => {
            const cb = instance.findByType(Checkbox)
            expect(cb).toBeDefined()
            expect(cb.props.id).toEqual(props.field.name)
            expect(cb.props.checked).toEqual(props.value)
            expect(cb.props.disabled).toEqual(props.disabled)
        })

        it("does not render form helper text when there are no errors", () => {
            const helpText = instance.findAllByType(FormHelperText)
            expect(helpText.length).toEqual(0)
        })

        it("applies passed-in style", () => {
            const outerDiv = instance.findAll(el => el.type == "div")[0]
            expect(outerDiv.props.style).toEqual({marginTop: 10})
        })

    })

    describe("readonly mode", () => {
        const props = getComponentProps()
        props.readonly = true

        beforeAll(() => {
            render(props)
        })

        it("renders an FormControlLabel component containing props.label and no error", () => {
            const label = instance.findByType(FormControlLabel)
            expect(label).toBeDefined()
            expect(label.props.label).toEqual(props.label)
            expect(label.props.label).not.toEqual(props.field.label)
        })

        it("Renders a disabled Checkbox component with current value", () => {
            const cb = instance.findByType(Checkbox)
            expect(cb).toBeDefined()
            expect(cb.props.checked).toEqual(props.value)
            expect(cb.props.disabled).toEqual(props.readonly)
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
