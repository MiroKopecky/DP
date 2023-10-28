import pandas as pd
import matplotlib.pyplot as plt

# Load the CSV file into a Pandas DataFrame
df = pd.read_csv('FLICK DOWN/gyr_flick_down_fast.csv')

df.index = df.index / 100

# Extract the x, y, and z columns as separate arrays
x = df['x']
y = df['y']
z = df['z']

#subset_1 = df[(x > -1.5) & (x < 2.5) & (y > -2) & (y < 2) & (z > 9)] #DEFAULT
#subset_2 = df[(x > -2.5) & (x < 1) & (y < 9) & (z > -6.5) & (z < -4)] #FLICK UP
#subset_2 = df[(x > 9) & (y > -3.5) & (y < 4.5) & (z > -3) & (z < 3)] #UP
#subset_2 = df[(x < -8) & (y > -2) & (y < 2) & (z > 2) & (z < 6)] #DOWN

subset_1 = df[(x > -1) & (x < 1) & (y > -1) & (y < 1) & (z > -1) & (z < 1)] #DEFAULT
subset_2 = df[(x < -4) | (x > 5)] #FLICK UP/FLICK DOWN GYR
#subset_2 = df[(y < -3) | (y > 4)] #UP/DOWN GYR
#subset_2 = df[(z < -3) | (z > 3)] #LEFT/RIGHT GYR

fig, ax = plt.subplots()
ax.plot(x, c='#4286f4', label='x') # blue
ax.plot(y, c='#6699cc', label='y') # light blue
ax.plot(z, c='#003366', label='z') # dark blue


ymin = df.min().min()
ymax = df.max().max()
plt.vlines(x=subset_1.index, ymin=ymin, ymax=ymax, linewidth=3.5, color='grey', alpha=0.2)
plt.vlines(x=subset_2.index, ymin=ymin, ymax=ymax, linewidth=3.5, color='green', alpha=0.2)

# Add a scatter plot for the minimum and maximum points of the 'x' column
ax.scatter(x.idxmin(), x.min(), c='red', s=50)
ax.annotate(f"{x.min():.2f}", (x.idxmin(), x.min()), xytext=(10, -5),
            textcoords='offset points', fontsize=10, color='red')
ax.scatter(x.idxmax(), x.max(), c='red', s=50)
ax.annotate(f"{x.max():.2f}", (x.idxmax(), x.max()), xytext=(10, 0),
            textcoords='offset points', fontsize=10, color='red')

# Add a title to the plot
# Rýchly
# Prirodzený
# Pomalý
ax.set_title('Rýchly pohyb gesta FLICK DOWN')

# Add a title of axis
ax.set_xlabel('Čas (s)')
ax.set_ylabel('Uhlová rýchlosť (rad/s)')

# Add a legend to the plot
from matplotlib.patches import Patch
handles, labels = ax.get_legend_handles_labels()
handles.append(Patch(facecolor='grey', edgecolor='black', alpha=0.2, label='Základná poloha'))
handles.append(Patch(facecolor='green', edgecolor='black', alpha=0.2, label='Hraničná poloha'))
handles.append(Patch(facecolor='white', edgecolor='black', alpha=0.2, label='Medzipoloha'))
labels.append('Základná poloha')
labels.append('Hraničná poloha')
labels.append('Medzipoloha')
ax.legend(handles=handles, labels=labels)

# Show the plot
plt.show()
