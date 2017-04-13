The android application code was created in response to a code challenge from GetAround.
Initially an effort to incorporate the full process of OAuth1 authentication.  But this
turned out to be unnecessary.  The specification given is the following:

Screen One: Gallery View
• A grid of square thumbnails pulled from the 500px.com API,arranged two columns wide.
• You can use the Getaround API Keys:
• The photos should be pulled from the popular photos list, and filtered to only include photos in the "Nature" category.
• Note that if you do not filter the list, some photos may contain nudity.
• While your app is loading the list of popular photos, it should show a loading indicator.
• Tapping on a photo should show the Photo View for that photo.
• Bonus Feature (optional): Make it possible to load additional
photos by scrolling to the bottom of the view and tapping a “Load more photos” button.

Screen Two: Photo View
• A high resolution view of the Photo that was tapped.
• Pressing the back button should return to the Gallery View.
• Bonus Feature (optional): Use a progress indicator to show
image download progress.
