package de.lathanda.eos.geo;

import java.awt.Color;
import java.util.LinkedList;

import de.lathanda.eos.base.FillStyle;
import de.lathanda.eos.base.LineStyle;
import de.lathanda.eos.base.Picture;
import de.lathanda.eos.base.layout.BalancePoint;
import de.lathanda.eos.base.layout.BoundingBox;
import de.lathanda.eos.base.layout.Transform;
import de.lathanda.eos.geo.exceptions.RecursiveGroupException;
import de.lathanda.eos.util.ConcurrentLinkedList;

/**
 * Gruppen sind Kontainer für Figuren. Diese erlauben es mehrere Figuren
 * gemeinsam zu verändern. Jede Gruppe verwaltet ihr eigenes relatives
 * Koordinatensystem. Deshalb verhalten sich alle Aufrufe an eine Figur
 * innerhalb einer Gruppe relativ zum Gruppen Koordinatensystem.
 * 
 *
 * @author Peter (Lathanda) Schneider
 */
public class Group extends Figure implements FigureGroup {
	protected ConcurrentLinkedList<Figure> members;
	protected boolean autoCenter = true;

	public Group() {
		super();
		members = new ConcurrentLinkedList<Figure>();
	}

	@Override
	protected void scaleInternal(double factor) {
		transform = transform.scale(factor);
	}

	@Override
	public void addFigure(Figure figure) {
		//check recursive loop		
		Group grp = this.getGroup();
		while (grp != null) {
			if (grp == this) {
				throw new RecursiveGroupException();
			} else {
				grp = grp.getGroup();
			}
		}
		
		figure.replaceGroup(this);
		members.add(figure);
		figure.changeTransformation(this);
		fireLayoutChanged();
	}

	public void copyFigure(Figure figure) {
		Figure copy = figure.copy();
		addFigure(copy);
	}

	@Override
	public void removeFigure(Figure go) {
		members.remove(go);
		go.restoreTransformation(this);
		fireLayoutChanged();
	}

	/**
	 * Entfernt alle Figuren
	 */
	public void removeAll() {
		for (Figure f : members) {
			f.restoreTransformation(this);
		}
		members.clear();
		fireLayoutChanged();
	}

	@Override
	public Group getGroup() {
		return this;
	}

	@Override
	protected void drawObject(Picture p) {
		for (Figure m : members) {
			m.draw(p);
		}
	}

	@Override
	protected void fireLayoutChanged() {
		recenter();
		super.fireLayoutChanged();
	}

	public void setLineColor(Color color) {
		for (Figure m : members) {
			if (m instanceof LineFigure) {
				((LineFigure) m).setLineColor(color);
			} else if (m instanceof Group) {
				((Group) m).setLineColor(color);
			}
		}
	}

	public void setLineStyle(LineStyle linetype) {
		for (Figure m : members) {
			if (m instanceof LineFigure) {
				((LineFigure) m).setLineStyle(linetype);
			} else if (m instanceof Group) {
				((Group) m).setLineStyle(linetype);
			}
		}
	}

	public void setLineWidth(double linewidth) {
		for (Figure m : members) {
			if (m instanceof LineFigure) {
				((LineFigure) m).setLineWidth(linewidth);
			} else if (m instanceof Group) {
				((Group) m).setLineWidth(linewidth);
			}
		}
	}

	public void setFillStyle(FillStyle filltype) {
		for (Figure m : members) {
			if (m instanceof FilledFigure) {
				((FilledFigure) m).setFillStyle(filltype);
			} else if (m instanceof Group) {
				((Group) m).setFillStyle(filltype);
			}
		}
	}

	public void setFillColor(Color color) {
		for (Figure m : members) {
			if (m instanceof FilledFigure) {
				((FilledFigure) m).setFillColor(color);
			} else if (m instanceof Group) {
				((Group) m).setFillColor(color);
			}
		}
	}

	public void mirrorY() {
		transformMirrorY();
		fireLayoutChanged();
	}

	public void mirrorX() {
		transformMirrorX();
		fireLayoutChanged();
	}

	@Override
	public Figure copy() {
		Group g = (Group) super.copy();
		g.members = new ConcurrentLinkedList<Figure>();
		for (Figure m : members) {
			g.members.add(m.copy());
		}
		return g;
	}

	private void setCenterInternal(double x, double y) {
		synchronized (this) {
			double dx = getX() - x;
			double dy = getY() - y;
			members.forEach(figure -> figure.moveInternal(dx, dy));
			moveToInternal(x, y);
		}
	}

	/**
	 * defines the middle of the group. This will implicitly deactivate automatic
	 * centering. To reactivate it, call centerBalancePoint or a similar method.
	 * 
	 * @param x
	 * @param y
	 */
	public void setCenter(double x, double y) {
		autoCenter = false;
		setCenterInternal(x, y);
	}

	public void centerBalancePoint() {
		autoCenter = true;
		recenter();
	}

	private void recenter() {
		if (!autoCenter) {
			return;
		}
		BalancePoint wp = getBalancePoint();
		setCenterInternal(wp.getPoint().getX() + getX(), wp.getPoint().getY() + getY());
	}

	@Override
	protected BalancePoint getBalancePoint() {
		double weight = 0;
		double x = 0;
		double y = 0;
		BalancePoint bp;
		for (Figure figure : members) {
			bp = figure.getBalancePoint();
			double figureWeight = bp.getWeight();
			if (figureWeight > 0) {
				weight += figureWeight;
				x += bp.getX() * figureWeight;
				y += bp.getY() * figureWeight;
			}
		}

		if (weight != 0) {
			return new BalancePoint(weight, x / weight, y / weight);
		} else {
			return new BalancePoint(0, 0, 0);
		}
	}

	@Override
	protected BoundingBox calculateBoundingBox(Transform base, Transform own) {
		Transform t = base.transform(own);
		BoundingBox bound = new BoundingBox();
		for (Figure figure : members) {
			bound.add(figure.calculateBoundingBox(t));
		}
		return bound;
	}

	@Override
	public void getAttributes(LinkedList<Attribut> attributes) {
		super.getAttributes(attributes);
		attributes.add(new Attribut("autocenter", autoCenter));
		attributes.add(new Attribut("members", members));
	}
}
