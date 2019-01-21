
export interface IMenuItem {
    id: string
    label: string
    icon: string
    subItems?: IMenuSubItem[]
}

export interface IMenuSubItem {
    label: string
    perspective: string
    viewName?: string
}
