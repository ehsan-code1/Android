package com.example.myapplication

class Cars {

        private var makeName: String? = null
        private var modelName: String? = null
        private var modelID: String? = null
        private var makeID: String? = null


        constructor(makeID: String?, makeName: String?, modelId: String?, modelName: String?)  {
            this.makeID = makeID;
            this.makeName = makeName;
            this.modelID = modelId;
            this.modelName = modelName
        }

        fun returnMake(): String? {
            return this.makeName
        }

        fun returnModel(): String? {
            return this.modelName
        }
        fun returnModelID(): String? {
            return modelID
        }
        fun returnMakeID(): String? {
            return makeID
        }

}