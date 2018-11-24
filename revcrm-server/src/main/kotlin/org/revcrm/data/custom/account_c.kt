package org.revcrm.data.custom

// Create custom entity fields container

//fun registerAccountC(metadata: MetadataSources) {
//
//    // Lets try adding a custom entity
//    // http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd
//
//    val doc = DocumentBuilderFactory
//            .newInstance()
//            .newDocumentBuilder()
//            .newDocument()
//
//    // TODO: We should just build an XML string, since we convert to a byteArray anyway
//    val mappings = doc.createElement("hibernate-mapping")
//    doc.appendChild(mappings)
//
//    val accountCustom = doc.createElement("class")
//    accountCustom.setAttribute("entity-name", "account_c")
//    mappings.appendChild(accountCustom)
//    val idField = doc.createElement("id")
//    idField.setAttribute("name", "accountId")
//    idField.setAttribute("type", "int")
////    idField.setAttribute("lazy", "true")
//    accountCustom.appendChild(idField)
//        val idGenerator = doc.createElement("generator")
//        idGenerator.setAttribute("class", "foreign")
//        idField.appendChild(idGenerator)
//            val genParam = doc.createElement("param")
//            genParam.setAttribute("name", "property")
//            genParam.setTextContent("account")
//            idGenerator.appendChild(genParam)
//    val relField = doc.createElement("one-to-one")
//    relField.setAttribute("name", "account")
//    relField.setAttribute("class", "org.revcrm.models.accounts.Account")
//    relField.setAttribute("constrained", "true")
//    accountCustom.appendChild(relField)
//    val nameField = doc.createElement("property")
//    nameField.setAttribute("name", "custom_name")
//    nameField.setAttribute("type", "string")
//    accountCustom.appendChild(nameField)
//
//    val outputStream = ByteArrayOutputStream()
//
//    val xmlTransformer = TransformerFactory.newInstance().newTransformer()
//    xmlTransformer.transform(DOMSource(doc), StreamResult(outputStream))
//
//    metadata.addInputStream(ByteArrayInputStream(outputStream.toByteArray()))
//}

// Test code for custom entity
//log.info("Creating an account...")
//val name = "Test Account ${randomString(5)}"
//
//db.withTransaction { em ->
//    val account = Account(
//        is_org = true,
//        org_name = name
//    )
//    val account_c = mapOf(
//        "account" to account,
//        "custom_name" to name
//    )
//    em.persist(account)
//    em.session.persist("account_c", account_c)
//}
//
//log.info("Restarting hibernate...")
//db.reinitialise(dbConfig, entityList)
//
//db.withTransaction { em ->
//    val account = em.find(Account::class.java, 1)
//    val account_c = em.session.get("account_c", 1)
//    if (account != null && account_c != null && account_c is Map<*,*>) {
//        println("**************************")
//        println("Got account: ${account.org_name}")
//        println("Got account_c: ${account_c.get("custom_name")}")
//        println("**************************")
//    }
//}