package com.example.myapplication.car_database

/**
 * Custom Collection that Stores makeName,modelName,modelId and makeID
 * Getters to return each variable
 */
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

    /**
     * Returns makeName
     * @return String?
     */
        fun returnMake(): String? {
            return this.makeName
        }
    /**
     * Returns modelName
     * @return String?
     */
        fun returnModel(): String? {
            return this.modelName
        }
    /**
     * Returns modelID
     * @return String?
     */
        fun returnModelID(): String? {
            return modelID
        }
    /**
     * Returns makeID
     * @return String?
     */
        fun returnMakeID(): String? {
            return makeID
        }

}