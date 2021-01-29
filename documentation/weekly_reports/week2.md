# Report for week 1

**Hours: 14** 

### Stuff done during this week:

- Implemented the dungeon generator algorithm
  - Basic benchmarking with sample data
  - Testing (will do more)
- JavaFX GUI
  - Lots of experimenting with it
  - Custom Sliders and LongField
- ImageUtil to handle generating, scaling and saving images
  - Started testing
- Automatic testing with GitHub Actions and CodeCov
- Documentation
  - JavaDocs
    
Basically the project has progressed very nicely. I learned a lot about JavaFX which might have not been the actual goal implementing the algorithm was also a good experience to have. 

I might have coded a little more than I planned last week.

### "Problems"

On trying to save the current randomized seed for the purpose of showing that to the user, this happened: 

I tried to extract the seed from the Java's Random class, which worked! ...with the caveat of `WARNING: An illegal reflective access operation has occurred`. 

Luckily my friend pointed out that I could just take a random number (long) as the seed before running the actual Random class and show that one. I don't know how did I not think about that at first..

### Next week

Next week I will be improving the UI and doing testing/benchmarking. Also, I'll start to think about a simpler algorithm to make comparisons with.

### Examples of some dungeons

1. 200x200 px, 40 %, 2 steps, x & y scaled by 2, seed: 7482077468331371336
2. 200x200 px, 47 %, 5 steps, x & y scaled by 2, seed: 8684197241994208105

![preview of the application](../images/200x200_47%25_2steps_2x2scale_7482077468331371336.png)
![preview of the application](../images/200x200_40%25_5steps_2x2scale_8684197241994208105.png)