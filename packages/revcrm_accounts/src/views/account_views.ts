
// perspectives:
//     accounts: {
//         name: 'accounts',
//         title: 'Accounts',
//         views: {
//             list: 'account_list',
//             form: 'account_form'
//         }
//     }

// views:
// account_list: {
//     name: 'account_list',
//     model: 'Account',
//     component: (
//         <CRMListView fields={[
//             'id',
//             'name',
//             'code',
//             'website']} detailView="accounts/form" />
//     ),
// },
// account_form: {
//     name: 'account_form',
//     model: 'Account',
//     component: (
//         <CRMFormView>
//             <Panel title="Account Summary" colspan={12}>
//                 <Field name="name" colspan={9} />
//                 <Field name="code" colspan={3} />
//             </Panel>
//             <Panel title="Contact Details">
//                 <Field name="phone" colspan={12} />
//                 <Field name="mobile" colspan={12} />
//                 <Field name="email" colspan={12} />
//                 <Field name="website" colspan={12} />
//             </Panel>
//         </CRMFormView>
//     )
// }
