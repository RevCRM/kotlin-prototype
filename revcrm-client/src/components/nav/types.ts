
export interface IMenuItem {
    id: string
    label: string
    perspective?: string
    view?: string
    icon: string
    subItems?: IMenuSubItem[]
}

export interface IMenuSubItem {
    label: string
    perspective: string
    view?: string
}
