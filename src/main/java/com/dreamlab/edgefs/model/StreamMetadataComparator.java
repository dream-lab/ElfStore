package com.dreamlab.edgefs.model;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamlab.edgefs.thrift.DynamicTypeStreamMetadata;
import com.dreamlab.edgefs.thrift.I64TypeStreamMetadata;
import com.dreamlab.edgefs.thrift.StreamMetadata;

/**
 * Lets compare the current StreamMetadata with the submitted StreamMetadata and
 * check if the required objects are the same. In this we will not be comparing
 * the version of both the objects since that can be done separately to check
 * only the versions. This class provides only the semantic checker regarding
 * the updates to the field
 *
 */
public class StreamMetadataComparator {

	private static final Logger LOGGER = LoggerFactory.getLogger(StreamMetadataComparator.class);

	// this field is used for comparing the value when the field
	// is updatable
	private static final String FIELD_VALUE = "value";

	// this is the name of the field which should remain the same
	// during updates
	private static final String FIELD_UPDATABLE = "updatable";

	// this field is not checked as we are doing semantic checking
	private static final String FIELD_VERSION = "version";

	// there is no need to check streamId as well so skipping it as well
	private static final String FIELD_STREAM_ID = "streamId";

	// it might happen that the incoming object doesn't have the owner
	// field set, the current one will definitely have so we may need to
	// ignore this field check as well
	private static final String FIELD_OWNER = "owner";

	private static final String FIELD_ENDTIME = "endTime";

	// lets deal with all other properties in a slightly different way
	private static final String FIELD_OTHER_PROPS = "otherProperties";

	// the order of arguments here matter. The first one is the current metadata
	// at the Fog and second is the client supplied one
	public static boolean compare(StreamMetadata metadata1, StreamMetadata metadata2) {
		Field[] fields = StreamMetadata.class.getDeclaredFields();
		for (Field fd : fields) {
			// this is to prevent comparing on some thrift generated fields
			// which start with underscore
			if (fd.getName().startsWith("_"))
				continue;
			// no need to match the static fields
			if (Modifier.isStatic(fd.getModifiers()))
				continue;
			if (fd.getName().equals(FIELD_VERSION) || fd.getName().equals(FIELD_STREAM_ID))
				continue;
			// if its the owner field and client has not set it, move to the next field
			if (fd.getName().equals(FIELD_OWNER) && !metadata2.isSetOwner())
				continue;
			// some special handling for endTime field, endTime is not an updatable field
			// it will be set only once when the stream is closed
			if (fd.getName().equals(FIELD_ENDTIME)) {
				if (!metadata1.isSetEndTime()) {
					continue;
				} else {
					LOGGER.error("No updates possible on a closed stream, hence the error");
					return false;
				}
			}

			if (!fd.getName().equals(FIELD_OTHER_PROPS)) {
				try {
					Object fieldObj1 = fd.get(metadata1);
					Object fieldObj2 = fd.get(metadata2);
					// now we can rely for comparison on two fields: value and updatable
					// IMPORTANT NOTE:: All the classes that are used inside of StreamMetadata
					// have fields no more than value and updatable (except for
					// DynamicTypeStreamMetadata which is handled different as shown in the if
					// condition below). This is a careful design decision which allows us to do
					// what we are doing right now i.e. relying on comparing two objects based
					// on only these two fields
					Field updatableField = fd.getType().getDeclaredField(FIELD_UPDATABLE);
					// I am assuming here that for every field that is given while stream
					// creation or any field added afterwards, it is not possible to update
					// the updatable flag of that field. Its like enforcing that metadata of
					// the field cannot be changed, only the value can be changed
					if (updatableField.getBoolean(fieldObj1) != updatableField.getBoolean(fieldObj2)) {
						LOGGER.error("The updatable flag is changed for field : {}, this is not allowed"
								+ ", hence rejecting the update with an error", fd.getName());
						return false;
					}
					// now check if the update is allowed or not
					if (!updatableField.getBoolean(fieldObj1)) {
						Field valueField = fd.getType().getDeclaredField(FIELD_VALUE);
						// since field is marked as not updatable, in case the values between
						// the two objects are different, then this is an error
						if (!valueField.get(fieldObj1).equals(valueField.get(fieldObj2))) {
							LOGGER.error("Value for field : {} marked as not updatable is modified by"
									+ " the client, hence rejecting the update", fd.getName());
							return false;
						}
					}
				} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
						| SecurityException ex) {
					LOGGER.error("Error occured during semantic checking for stream metadata updates", ex);
					ex.printStackTrace();
					return false;
				}
			} else {
				if (!compareDynamicProperties(metadata1.getOtherProperties(), metadata2.getOtherProperties())) {
					return false;
				} else {
					continue;
				}
			}
		}
		return true;
	}

	private static boolean compareDynamicProperties(Map<String, DynamicTypeStreamMetadata> currentProperties,
			Map<String, DynamicTypeStreamMetadata> updatedProperties) {
		// currently I am taking two cases when a property is present in only one
		// 1: if present in the first means property is being deleted
		// 2: if present in the second means property is being added
		// case 2 is always valid as new properties can be added anytime but case 1
		// is only valid when the field was marked as updatable and that allows the
		// client to delete as well. Case 1 becomes invalid when updates are not allowed
		if(currentProperties == null)
			return true;
		if(updatedProperties == null)
			updatedProperties = new HashMap<>();
		
		for (String key : currentProperties.keySet()) {
			DynamicTypeStreamMetadata dynamicMetadata1 = currentProperties.get(key);
			if (!updatedProperties.containsKey(key)) {
				// field missing from new object and if the field was not updatable
				// this is an error
				if (!dynamicMetadata1.isUpdatable()) {
					LOGGER.error("Dynamic field : {} which is not updatable cannot be deleted"
							+ ", hence rejecting the stream metadata update", key);
					return false;
				}
			} else {
				DynamicTypeStreamMetadata dynamicMetadata2 = updatedProperties.get(key);
				// field exists in both, then the updatable field in both must be the same
				// as this updatable flag cannot be changed, see the meta meta comment above
				if (dynamicMetadata1.isUpdatable() != dynamicMetadata2.isUpdatable()) {
					LOGGER.error("The updatable flag is changed for field : {}, this is not allowed"
							+ ", hence rejecting the update with an error", key);
					return false;
				}
				if (!dynamicMetadata1.isUpdatable()) {
					if (!dynamicMetadata1.getValue().equals(dynamicMetadata2.getValue())) {
						LOGGER.error("Dynamic field : {} which is not updatable cannot be updated"
								+ ", hence rejecting the stream metadata update", key);
						return false;
					}
				}
			}
		}
		return true;
	}

}
