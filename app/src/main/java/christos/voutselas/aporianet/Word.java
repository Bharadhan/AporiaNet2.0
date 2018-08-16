package christos.voutselas.aporianet;

/**
 * {@link Word} represents a list of lessons that the user can choose.
 * It contains resource IDs lesson direction, lesson name and
 * optional image file for that word.
 */
public class Word
{
    /** String resource ID for the direction of this lesson */
    private int mLessonDirectionId;

    /** String resource ID for name of this lesson */
    private int mLessonNameId;

    /** String resource ID for year of the class */
    private int mYearClassId;

    /** Image resource ID for the lesson */
    private int mImageResourceId = NO_IMAGE_PROVIDED;

    /** Constant value that represents no image was provided for this lesson */
    private static final int NO_IMAGE_PROVIDED = -1;

    /**
     * Create a new Word object.
     *
     * @param lessonDirectionId is the string resource ID for the lesson direction
     * @param lessonNameId is the string resource Id for the lesson name
     */
    public Word(int lessonDirectionId, int lessonNameId, int yearClass)
    {
        mLessonDirectionId = lessonDirectionId;
        mLessonNameId = lessonNameId;
        mYearClassId = yearClass;
    }

    /**
     * Create a new Word object.
     *
     * @param lessonDirectionId is the string resource ID for the lesson direction
     * @param lessonNameId is the string resource Id for the lesson name
     * @param imageResourceId is the drawable resource ID for the image associated with the word
     */
    public Word(int lessonDirectionId, int lessonNameId, int imageResourceId, int yearClass)
    {
        mLessonDirectionId = lessonDirectionId;
        mLessonNameId = lessonNameId;
        mImageResourceId = imageResourceId;
        mYearClassId = yearClass;
    }

    /**
     * Get the string resource ID for the direction of this lesson.
     */
    public int getLessonDirectionId() {
        return mLessonDirectionId;
    }

    /**
     * Get the string resource ID for name of this lesson.
     */
    public int getLessonNameId() {
        return mLessonNameId;
    }

    /**
     * Get the string resource ID for year clas.
     */
    public int getYearClassId() {
        return mYearClassId;
    }

    /**
     * Return the image resource ID of the lesson.
     */
    public int getImageResourceId() {
        return mImageResourceId;
    }

    /**
     * Returns whether or not there is an image for this lesson.
     */
    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }
}